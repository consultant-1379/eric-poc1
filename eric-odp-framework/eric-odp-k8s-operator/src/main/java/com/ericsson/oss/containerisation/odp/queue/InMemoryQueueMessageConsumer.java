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

import java.io.IOException;
import java.util.Queue;

import org.jboss.logging.Logger;

import com.ericsson.nms.client.ldap.ActiveDirectoryConnector;
import com.ericsson.nms.client.service.LdapClientServiceImpl;
import com.ericsson.nms.client.util.UserDetails;
import com.ericsson.oss.containerisation.odp.k8s.K8sHandler;
import com.ericsson.oss.containerisation.odp.k8s.PodInfo;
import com.ericsson.oss.containerisation.odp.util.Utils;

public class InMemoryQueueMessageConsumer implements Runnable {

    private static final Logger LOGGER = Logger.getLogger(InMemoryQueueMessageConsumer.class);

    private Queue<JobCreationRequestConfig> queue;
    private K8sHandler k8sHandler;
    private LdapClientServiceImpl ldapClientService;

    public InMemoryQueueMessageConsumer(final Queue<JobCreationRequestConfig> queue, final K8sHandler k8sHandler,
	    final ActiveDirectoryConnector connector) {
	this.queue = queue;
	this.k8sHandler = k8sHandler;
	this.ldapClientService = new LdapClientServiceImpl(connector);
    }

    @Override
    public void run() {
	consume();
    }

    private void consume() {
	while (true) {
	    final JobCreationRequestConfig jobCreationRequestConfig = queue.poll();
	    if (jobCreationRequestConfig != null) {
		processJobCreationRequestConfig(jobCreationRequestConfig);
	    }
	}
    }

    private void processJobCreationRequestConfig(final JobCreationRequestConfig jobCreationRequestConfig) {
	try {
	    // Additional check to see if the job exists or not. This is added here as well
	    // just to be sure that there is no job already running/scheduled.
	    final PodInfo podInfo = k8sHandler
		    .checkPodExists(Utils.getAppLabel(jobCreationRequestConfig.getServiceName(), jobCreationRequestConfig.getUserName()));
	    if (podInfo == null) {
		final UserDetails userDetails = ldapClientService.getUserDetailsForUser(jobCreationRequestConfig.getUserName());
		LOGGER.debugf("Details retrieved for user [%s] from LDAP is [%s]", jobCreationRequestConfig.getUserName(), userDetails);
		k8sHandler.createJobWithYamlTemplate(jobCreationRequestConfig, userDetails);
	    } else {
		LOGGER.debugf("Job is already scheduled for user [%s]. Ignoring the current request.", jobCreationRequestConfig.getUserName());
	    }
	} catch (final Exception exception) {
	    LOGGER.error("Exception in processJobCreationRequestConfig for user[%s] is [%s]", jobCreationRequestConfig.getUserName(), exception );
	}
    }

}
