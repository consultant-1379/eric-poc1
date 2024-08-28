package com.ericsson.nms.client.service;

import com.ericsson.nms.client.ldap.ActiveDirectoryConnector;
import com.ericsson.nms.client.util.UserDetails;
import org.forgerock.opendj.ldap.ErrorResultException;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface LdapClientService {

    UserDetails getUserDetailsForUser(String userName) throws IOException;

    List<Integer> getSupplementaryGIdsForUser(String userName) throws IOException;

    Map getSupplementaryGIdsWithNameForUser(String userName) throws IOException;

    String getInitGroupsForUser(String userName) throws IOException;

}
