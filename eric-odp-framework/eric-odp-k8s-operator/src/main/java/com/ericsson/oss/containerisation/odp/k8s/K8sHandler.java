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

import static com.ericsson.oss.containerisation.odp.util.Constants.APP_LABEL;

import java.util.List;

import org.jboss.logging.Logger;

import com.ericsson.nms.client.util.UserDetails;
import com.ericsson.oss.containerisation.odp.queue.JobCreationRequestConfig;
import com.ericsson.oss.containerisation.odp.service.OnDemandPodService;
import com.ericsson.oss.containerisation.odp.service.OnDemandPodServiceProvider;

import io.fabric8.kubernetes.api.model.ContainerState;
import io.fabric8.kubernetes.api.model.ContainerStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.batch.v1.Job;
import io.fabric8.kubernetes.client.KubernetesClient;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class K8sHandler {

    private static final Logger LOGGER = Logger.getLogger(K8sHandler.class);

    @Inject
    private KubernetesClient client;

    public PodInfo checkPodExists(final String appIdentifier) {
	PodInfo podInfo = null;
	final List<Pod> podList = client.pods().withLabel(APP_LABEL, appIdentifier).list().getItems();
	for (final Pod pod : podList) {
	    final String result = pod.getMetadata().getName();
	    podInfo = new PodInfo();
	    podInfo.setPodName(result);
	    final List<ContainerStatus> list = pod.getStatus().getContainerStatuses();
	    for (ContainerStatus containerstatus : list) {
		// Return the main container state.
		updatePodInfoWithContainerState(containerstatus, podInfo);
		break;
	    }
	}
	return podInfo;
    }

    public void createJobWithYamlTemplate(final JobCreationRequestConfig jobCreationRequestConfig, final UserDetails userDetails) {
	final OnDemandPodService onDemandPodService = OnDemandPodServiceProvider.createFactory(jobCreationRequestConfig.getServiceName());
	final Job job = onDemandPodService.loadTemplate(jobCreationRequestConfig, userDetails);
	try {
	    client.batch().v1().jobs().resource(job).create();
	    LOGGER.infof("Succcessfully created K8s job for given request:[%s]", jobCreationRequestConfig);
	} catch (final Exception exception) {
	    LOGGER.error("Exception in job creation for request [%s] is [%s]", jobCreationRequestConfig, exception);
	}
    }

    private void updatePodInfoWithContainerState(final ContainerStatus containerStatus, final PodInfo podInfo) {
	podInfo.setPodState(containerStatus.getState().toString());
	podInfo.setPodStateCode(PodInfo.PodStateCode.WAITING);
	final ContainerState containerState = containerStatus.getState();
	if (containerState.getRunning() != null) {
	    podInfo.setPodStateCode(PodInfo.PodStateCode.RUNNING);
	} else if (containerState.getTerminated() != null) {
	    podInfo.setPodStateCode(PodInfo.PodStateCode.TERMINATED);
	}
    }

}
