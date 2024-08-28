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
package com.ericsson.nms.client.config;

import java.io.IOException;

/**
 * Configuration reader for ENIQ-S WAS (Windows Application Server) Integration
 *
 * @author ekarpia
 *
 */
public class LdapPropertiesReader extends ApplicationPropertiesReader {

    public static final String DEPLOYMENT_CONFIG = "/var/lib/eric-odp/ldap.properties";
    /**
     * @throws IOException
     */
    public LdapPropertiesReader() throws IOException {
        super(DEPLOYMENT_CONFIG);
    }

}
