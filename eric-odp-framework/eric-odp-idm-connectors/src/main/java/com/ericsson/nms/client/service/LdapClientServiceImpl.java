package com.ericsson.nms.client.service;

import com.ericsson.nms.client.ldap.ActiveDirectoryConnector;
import com.ericsson.nms.client.util.ActivityConnectorUtil;
import com.ericsson.nms.client.util.UserDetails;
import org.forgerock.opendj.ldap.ErrorResultException;
import org.forgerock.opendj.ldif.ConnectionEntryReader;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class LdapClientServiceImpl implements LdapClientService {
    private ActiveDirectoryConnector activeDirectoryConnector;

    public LdapClientServiceImpl(ActiveDirectoryConnector activeDirectoryConnector) {
        this.activeDirectoryConnector = activeDirectoryConnector;
    }


    @Override
    public UserDetails getUserDetailsForUser(String userName) throws IOException {

        UserDetails userDetails = null;
        try {
            userDetails = ActivityConnectorUtil.getUserDetailsFromSearchEntry(activeDirectoryConnector.getEntity(userName));
        } catch (ErrorResultException e) {
            throw new RuntimeException(e);
        }
        userDetails.setSupplementaryGIds(getSupplementaryGIdsForUser(userName));
        userDetails.setSupplementaryGroups(getSupplementaryGIdsWithNameForUser(userName));
        userDetails.setInitGroups(getInitGroupsForUser(userName));

        return userDetails;
    }

    @Override
    public List<Integer> getSupplementaryGIdsForUser(String userName) throws IOException {
        ConnectionEntryReader connectionEntryReader =
                activeDirectoryConnector.getGroupsForEntity(userName);
        return ActivityConnectorUtil.getUserSupplementaryGroupNumbers(connectionEntryReader);
    }

    @Override
    public Map getSupplementaryGIdsWithNameForUser(String userName) throws IOException {
        ConnectionEntryReader connectionEntryReader =
                activeDirectoryConnector.getGroupsForEntity(userName);
        return ActivityConnectorUtil.getUserSupplementaryGroupIds(connectionEntryReader);
    }

    @Override
    public String getInitGroupsForUser(String userName) throws IOException {
        ConnectionEntryReader connectionEntryReader =
                activeDirectoryConnector.getGroupsForEntity(userName);
        return ActivityConnectorUtil.getInitGroups(connectionEntryReader);
    }

}
