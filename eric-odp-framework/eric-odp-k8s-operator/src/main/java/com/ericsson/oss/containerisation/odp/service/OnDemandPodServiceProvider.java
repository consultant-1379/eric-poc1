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
package com.ericsson.oss.containerisation.odp.service;

public class OnDemandPodServiceProvider {

    private static final String AMOS = "amos";

    public static OnDemandPodService createFactory(final String serviceName) {
	if (AMOS.equalsIgnoreCase(serviceName))
	    return new AMOSOnDemandPodService();
	else
	    throw new RuntimeException(serviceName);
    }

}
