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

import com.ericsson.nms.client.util.UserDetails;
import com.ericsson.oss.containerisation.odp.queue.JobCreationRequestConfig;

import io.fabric8.kubernetes.api.model.batch.v1.Job;

public interface OnDemandPodService {

    Job loadTemplate(final JobCreationRequestConfig jobCreationRequestConfig, UserDetails userDetails);

    //Job createPod(String yamlConfig) throws IOException;

}
