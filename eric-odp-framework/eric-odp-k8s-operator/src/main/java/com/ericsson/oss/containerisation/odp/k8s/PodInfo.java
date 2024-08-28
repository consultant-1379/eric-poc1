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
package com.ericsson.oss.containerisation.odp.k8s;

public class PodInfo {

    private String podName;
    private PodStateCode podStateCode;
    private String podState;

    public String getPodName() {
	return podName;
    }

    public String getPodState() {
	return podState;
    }

    public void setPodName(String podName) {
	this.podName = podName;
    }

    public void setPodState(String state) {
	this.podState = state;
    }

    public PodStateCode getPodStateCode() {
        return podStateCode;
    }

    public void setPodStateCode(PodStateCode podStateCode) {
        this.podStateCode = podStateCode;
    }

    @Override
    public String toString() {
	return "PodInfo [podName=" + podName + ", podStateCode=" + podStateCode + ", podState=" + podState + "]";
    }


    public enum PodStateCode {
	RUNNING(0), TERMINATED(1), WAITING(2);

	public int stateCode;

	PodStateCode(int i) {
	    stateCode = i;
	}

    }

}
