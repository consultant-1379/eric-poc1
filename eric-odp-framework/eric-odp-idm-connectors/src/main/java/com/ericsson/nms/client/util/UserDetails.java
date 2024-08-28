package com.ericsson.nms.client.util;

import java.util.List;
import java.util.Map;

public class UserDetails {
    private String uId;
    private String gId;
    private Map supplementaryGroups;
    private List<Integer> supplementaryGIds;
    private String shell;
    private String homeDirectory;
    private Integer uidNumber;

    private String initGroups;

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getgId() {
        return gId;
    }

    public void setgId(String gId) {
        this.gId = gId;
    }


    public Map getSupplementaryGroups() {
        return supplementaryGroups;
    }

    public void setSupplementaryGroups(Map supplementaryGroups) {
        this.supplementaryGroups = supplementaryGroups;
    }

    public List<Integer> getSupplementaryGIds() {
        return supplementaryGIds;
    }

    public void setSupplementaryGIds(List<Integer> supplementaryGIds) {
        this.supplementaryGIds = supplementaryGIds;
    }

    public String getShell() {
        return shell;
    }

    public void setShell(String shell) {
        this.shell = shell;
    }

    public String getHomeDirectory() {
        return homeDirectory;
    }

    public void setHomeDirectory(String homeDirectory) {
        this.homeDirectory = homeDirectory;
    }

    public Integer getUidNumber() {
        return uidNumber;
    }

    public void setUidNumber(Integer uidNumber) {
        this.uidNumber = uidNumber;
    }

    public String getInitGroups() {
        return initGroups;
    }

    public void setInitGroups(String initGroups) {
        this.initGroups = initGroups;
    }

    @Override
    public String toString() {
        return "UserDetails{" +
                "uId='" + uId + '\'' +
                ", gId='" + gId + '\'' +
                ", supplementaryGroups=" + supplementaryGroups +
                ", supplementaryGIds=" + supplementaryGIds +
                ", shell='" + shell + '\'' +
                ", homeDirectory='" + homeDirectory + '\'' +
                ", uidNumber=" + uidNumber +
                ", initGroups=" + initGroups +
                '}';
    }
}
