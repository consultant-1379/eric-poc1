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
package com.ericsson.oss.containerisation.odp.rest;

import static com.ericsson.oss.containerisation.odp.util.Constants.ERROR_MESSAGE;
import static com.ericsson.oss.containerisation.odp.util.Constants.MESSAGE_JSON_FORMAT;
import static com.ericsson.oss.containerisation.odp.util.Constants.POD_ALREADY_EXISTS;
import static com.ericsson.oss.containerisation.odp.util.Constants.POD_EXISTS_MESSAGE_JSON_FORMAT;
import static com.ericsson.oss.containerisation.odp.util.Constants.SUCCESS_MESSAGE;

import org.jboss.logging.Logger;

import com.ericsson.oss.containerisation.odp.k8s.K8sHandler;
import com.ericsson.oss.containerisation.odp.k8s.PodInfo;
import com.ericsson.oss.containerisation.odp.queue.InMemoryQueueHandler;
import com.ericsson.oss.containerisation.odp.queue.JobCreationRequestConfig;
import com.ericsson.oss.containerisation.odp.util.Utils;

import jakarta.inject.Inject;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/eric-custom-operator/v1")
public class ODPK8sOperatorRestResource {

    private static final Logger LOGGER = Logger.getLogger(ODPK8sOperatorRestResource.class);

    @Inject
    private K8sHandler k8sHandler;

    @Inject
    private InMemoryQueueHandler inMemoryQueue;

    @PUT
    @Path("/k8s-resources/job/actions")
    public Response checkAndCreateJobOnDemand(final JobCreationRequestConfig jobCreationRequestConfig) {
        // Check if user pod exists or not.
        LOGGER.debugf("Received REST request with data:[%s]", jobCreationRequestConfig);
        final String userName = jobCreationRequestConfig.getUserName();
        final String serviceName = jobCreationRequestConfig.getServiceName();
        final String appLabel = Utils.getAppLabel(serviceName, userName);
        final PodInfo podInfo = k8sHandler.checkPodExists(appLabel);
        if (podInfo != null) {
            // Use the existing pod.
            final String responseBody = POD_EXISTS_MESSAGE_JSON_FORMAT
                    .format(new Object[] { POD_ALREADY_EXISTS, userName, podInfo.getPodName(),podInfo.getPodStateCode().stateCode });
            return Response.status(Status.OK).entity(responseBody).build();
        } else {
            // Check if the message queue has entry.
            if (inMemoryQueue.isUserExists(appLabel)) {
                // return error code
                final String responseBody = MESSAGE_JSON_FORMAT
                        .format(new Object[] { ERROR_MESSAGE, userName });
                return Response.status(Status.CONFLICT).entity(responseBody).build();
            } else {
                // Add token and username to the message queue
                inMemoryQueue.addToQueue(jobCreationRequestConfig);
                final String responseBody = MESSAGE_JSON_FORMAT
                        .format(new Object[] { SUCCESS_MESSAGE, userName });
                return Response.status(Status.CREATED).entity(responseBody).build();
            }
        }
    }

}
