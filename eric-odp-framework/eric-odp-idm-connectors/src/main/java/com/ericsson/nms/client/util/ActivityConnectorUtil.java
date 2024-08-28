package com.ericsson.nms.client.util;

import org.forgerock.opendj.ldap.Attribute;
import org.forgerock.opendj.ldap.ErrorResultIOException;
import org.forgerock.opendj.ldap.LinkedAttribute;
import org.forgerock.opendj.ldap.SearchResultReferenceIOException;
import org.forgerock.opendj.ldap.responses.SearchResultEntry;
import org.forgerock.opendj.ldif.ConnectionEntryReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

public class ActivityConnectorUtil {

    public static final String HOME_DIRECTORY = "homeDirectory";
    public static final String LOGIN_SHELL = "loginShell";
    public static final String UID = "uid";
    public static final String UID_NUMBER = "uidNumber";
    public static final String GID_NUMBER = "gidNumber";

    public static final String CIPHER_SUITE = "TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384";

    public static String getFormattedAttributeValue(String value) {
        return value.replaceAll("\"", "").replace("]", "").replace("[", "");
    }

    public static UserDetails getUserDetailsFromSearchEntry(SearchResultEntry searchResultEntry) {
        UserDetails userDetails = new UserDetails();
        final Iterable<Attribute> groupAttributes = searchResultEntry.getAllAttributes();

        final Iterator<Attribute> attributeIterator = groupAttributes.iterator();
        while (attributeIterator.hasNext()) {
            LinkedAttribute attribute = (LinkedAttribute) attributeIterator.next();
            String[] attrArr = attribute.toString().split(":");
            switch (attribute.getAttributeDescriptionAsString()) {
                case (HOME_DIRECTORY):
                    userDetails.setHomeDirectory(getFormattedAttributeValue(attrArr[1]));
                    break;
                case (LOGIN_SHELL):
                    userDetails.setShell(getFormattedAttributeValue(attrArr[1]));
                    break;
                case (UID_NUMBER):
                    userDetails.setUidNumber(Integer.valueOf(getFormattedAttributeValue(attrArr[1])));
                    break;
                case (UID):
                    userDetails.setuId(getFormattedAttributeValue(attrArr[1]));
                    break;
                case (GID_NUMBER):
                    userDetails.setgId(getFormattedAttributeValue(attrArr[1]));
                    break;
            }
        }
        return userDetails;
    }

    public static Map getUserSupplementaryGroupIds(ConnectionEntryReader connectionEntryReader) throws ErrorResultIOException, SearchResultReferenceIOException {
        Map<String, Integer> gidMap = new HashMap<>();
        while (connectionEntryReader.hasNext()) {
            SearchResultEntry searchResult = connectionEntryReader.readEntry();
            String groupName = searchResult.getName().toString().split("=")[1].split(",")[0];

            final Iterable<Attribute> groupAttributes = searchResult.getAllAttributes();

            for (Attribute groupAttribute : groupAttributes) {
                LinkedAttribute attribute = (LinkedAttribute) groupAttribute;
                if (attribute.getAttributeDescriptionAsString().contains(GID_NUMBER)) {
                    String gidNumber = getFormattedAttributeValue(attribute.toString()).split(":")[1];
                    gidMap.put(groupName, Integer.valueOf(gidNumber));
                }
            }
        }
        return gidMap;
    }

    public static String getInitGroups(ConnectionEntryReader connectionEntryReader) throws ErrorResultIOException, SearchResultReferenceIOException {
        StringBuilder stringBuilder = new StringBuilder();
        while (connectionEntryReader.hasNext()) {
            SearchResultEntry searchResult = connectionEntryReader.readEntry();
            String groupName = searchResult.getName().toString().split("=")[1].split(",")[0];
            final Iterable<Attribute> groupAttributes = searchResult.getAllAttributes();
            for (Attribute groupAttribute : groupAttributes) {
                LinkedAttribute attribute = (LinkedAttribute) groupAttribute;
                if (attribute.getAttributeDescriptionAsString().contains(GID_NUMBER)) {
                    String gidNumber = getFormattedAttributeValue(attribute.toString()).split(":")[1];
                    stringBuilder.append(groupName).append(":").append( Integer.valueOf(gidNumber)).append(";");
                }
            }
        }
        return stringBuilder.toString();
    }

    public static List<Integer> getUserSupplementaryGroupNumbers(ConnectionEntryReader connectionEntryReader) throws ErrorResultIOException, SearchResultReferenceIOException {
        List<Integer> gIdsList = new ArrayList<>();
        while (connectionEntryReader.hasNext()) {
            SearchResultEntry searchResult = connectionEntryReader.readEntry();
            final Iterable<Attribute> groupAttributes = searchResult.getAllAttributes();

            for (Attribute groupAttribute : groupAttributes) {
                LinkedAttribute attribute = (LinkedAttribute) groupAttribute;
                if (attribute.getAttributeDescriptionAsString().contains(GID_NUMBER)) {
                    String gidNumber = getFormattedAttributeValue(attribute.toString()).split(":")[1];
                    gIdsList.add(Integer.valueOf(gidNumber));
                }
            }
        }
        return gIdsList;
    }

}
