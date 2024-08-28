/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2017
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.nms.client.ldap;

import com.ericsson.nms.client.config.LdapPropertiesReader;
import org.forgerock.opendj.ldap.*;
import org.forgerock.opendj.ldap.requests.Requests;
import org.forgerock.opendj.ldap.responses.SearchResultEntry;
import org.forgerock.opendj.ldif.ConnectionEntryReader;

import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import static com.ericsson.nms.client.util.ActivityConnectorUtil.CIPHER_SUITE;

/**
 * Wrapper around the LDAP connection and functionality to make changes in
 * Active Directory using LDAP protocol
 *
 * @author zjmkaah
 */

// Connection and ldapFactory fields can not be made final
@SuppressWarnings("PMD.ImmutableField")
public class ActiveDirectoryConnector {

    private LDAPConnectionFactory ldapFactory = null;
    private Connection connection = null;

    private static final String LDAP_HOST = "ldap-local";

    private static final String GROUP_BASE = "ou=Groups";
    private static final String USER_BASE = "ou=People";
    private static final String GROUP_CN = "uniqueMember=uid=%s,ou=People";
    /**
     * Keeping these variables public so that these can be used in util classes
     */
    public static final String COM_INF_LDAP_PORT = "COM_INF_LDAP_PORT";
    public static final String LDAP_ADMIN_CN = "LDAP_ADMIN_CN";
    public static final String LDAP_BIND_PW = "LDAP_BIND_PW";
    public static final String COM_INF_LDAP_ROOT_SUFFIX = "COM_INF_LDAP_ROOT_SUFFIX";

    public ActiveDirectoryConnector() {
        final LdapPropertiesReader deploymentProperties;
        try {
            deploymentProperties = new LdapPropertiesReader();
            final int ldapPort = Integer.parseInt(deploymentProperties.getValue(COM_INF_LDAP_PORT));
            final String ldapUser = deploymentProperties.getValue(LDAP_ADMIN_CN).replace("\"", "");
            final String ldapPassword = deploymentProperties.getValue(LDAP_BIND_PW).replace("\"", "");

            this.ldapFactory = getLdapConnectionFactory(LDAP_HOST, ldapPort);
            this.connection = getAuthenticatedConnectionFactory(ldapFactory, ldapUser, ldapPassword).getConnection();
        } catch (final GeneralSecurityException | ErrorResultException | IOException exc) {
            throw new RuntimeException(exc);
        }
    }

    /**
     *
     */
    public void disconnect() {

        if (connection != null) {
            connection.close();
        }

        if (ldapFactory != null) {
            ldapFactory.close();
        }

    }

    private String getGetGroupsForEntityURI(final String userName, String entitiesBaseDN) {
        final StringBuilder strBuilder = new StringBuilder();
        strBuilder.append(String.format(GROUP_CN, userName));
        final String[] domainParts = entitiesBaseDN.split(",");
        if (domainParts.length > 2) {
            strBuilder.append(",").append(domainParts[1]).append(",").append(domainParts[2]);
        }
        return strBuilder.toString();
    }


    /**
     * @param userOrRoleName
     * @return
     * @throws IOException
     * @throws ErrorResultException
     */
    public SearchResultEntry getEntity(final String userOrRoleName) throws IOException, ErrorResultException {
        final SearchResultEntry entry = connection.searchSingleEntry(getBaseDN(USER_BASE), SearchScope.SINGLE_LEVEL,
                "(CN=" + userOrRoleName + ")");
        if (entry == null) {

            throw new IOException("Object is not present in Active Directory.");
        }

        return entry;
    }

    /**
     * @param userName
     * @return
     * @throws IOException
     * @throws ErrorResultException
     */
    public ConnectionEntryReader getGroupsForEntity(final String userName) throws IOException {
        ConnectionEntryReader entry = null;
        String entitiesBaseDN = getBaseDN(GROUP_BASE);
        String filterStr = getGetGroupsForEntityURI(userName, entitiesBaseDN);
        entry = connection.search(entitiesBaseDN, SearchScope.WHOLE_SUBTREE, "(" + filterStr + ")");
        if (entry == null) {
            throw new IOException("Object is not present in Active Directory.");
        }
        return entry;

    }

    // methods for obtaining connection

    /**
     * @param ldapHost
     * @param ldapPort
     * @return
     * @throws GeneralSecurityException
     */
    protected final LDAPConnectionFactory getLdapConnectionFactory(final String ldapHost, final int ldapPort)
            throws GeneralSecurityException {

        return new LDAPConnectionFactory(ldapHost, ldapPort, getLDAPOptions());
    }

    /**
     * Make authenticated connection factory from simple factory
     *
     * @param ldapConnectionFactory
     * @param ldapUser
     * @param ldapPassword
     * @return
     */
    protected final ConnectionFactory getAuthenticatedConnectionFactory(
            final LDAPConnectionFactory ldapConnectionFactory, final String ldapUser, final String ldapPassword) {
        return
                Connections.newHeartBeatConnectionFactory(
                        Connections.newAuthenticatedConnectionFactory(ldapConnectionFactory,
                                Requests.newSimpleBindRequest(ldapUser, ldapPassword.toCharArray())),
                        30,
                        TimeUnit.SECONDS
                );
    }

    /**
     * Configure TrustStore and TrustManager, disable StartTLS
     *
     * @return
     * @throws GeneralSecurityException
     */
    private LDAPOptions getLDAPOptions() throws GeneralSecurityException {
        final LDAPOptions ldapOptions = new LDAPOptions();

        // Create a trust manager that does not validate certificate chains
        final SSLContext sslContext = new SSLContextBuilder().setTrustManager(TrustManagers.trustAll()).getSSLContext();


        ldapOptions.setSSLContext(sslContext);

        //TORF-344755
        //when AD DS have TLSv1.0 disabled, without this code, connection fails

        //we enable ordering of protocols, starting from TLSv1.2
        //by default SSLEngine of OpenDJ SDK starts from the TLSv1.0, which is wrong
        //we disable TLSv1.0, as not supported by e.g. AT&T and Deutsche Telekom
        ldapOptions.addEnabledProtocol("TLSv1.2");
        /** TODO this is hard-coded cipher and we will need to fix that in future **/
        ldapOptions.addEnabledCipherSuite(CIPHER_SUITE);

        ldapOptions.setUseStartTLS(false);
        ldapOptions.setTimeout(60, TimeUnit.SECONDS);

        return ldapOptions;
    }


    public ConnectionEntryReader getWithFilter(final String filter, SearchScope searchScope, String baseDN) throws IOException, ErrorResultException {
        final ConnectionEntryReader entry = connection.search(baseDN, searchScope,
                "(" + filter + ")");

        if (entry == null) {
            throw new IOException("Object is not present in Active Directory.");
        }

        return entry;
    }

    private String getBaseDN(String groupDN) {
        final LdapPropertiesReader deploymentProperties;
        try {
            deploymentProperties = new LdapPropertiesReader();
            return groupDN + "," + deploymentProperties.getValue(COM_INF_LDAP_ROOT_SUFFIX).replace("\"", "");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
