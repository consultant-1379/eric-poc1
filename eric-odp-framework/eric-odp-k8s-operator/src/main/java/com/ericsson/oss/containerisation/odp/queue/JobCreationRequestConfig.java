/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2024
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.oss.containerisation.odp.queue;

public class JobCreationRequestConfig {

    private String userName;
    private String ssoToken;
    private String serviceName;

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public void setUserName(final String userName) {
        this.userName = userName;
    }

    public void setSsoToken(final String ssoToken) {
        this.ssoToken = ssoToken;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getSsoToken() {
        return this.ssoToken;
    }

    @Override
    public String toString() {
	return "JobCreationRequestConfig [userName=" + userName + ", ssoToken=" + ssoToken + ", serviceName=" + serviceName + "]";
    }

}
