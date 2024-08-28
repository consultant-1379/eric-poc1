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

import static com.ericsson.oss.containerisation.odp.util.Constants.HYPHEN;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.ericsson.nms.client.ldap.ActiveDirectoryConnector;
import com.ericsson.oss.containerisation.odp.k8s.K8sHandler;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
@Startup
public class InMemoryQueueHandler {

    private Queue<JobCreationRequestConfig> queue = new ConcurrentLinkedQueue<>();
    private int consumerCount = 10;

    private ExecutorService executorService;
    private ActiveDirectoryConnector activeDirectoryConnector;

    @Inject
    private K8sHandler k8sHandler;

    @PostConstruct
    public void createConsumers() {
        executorService = Executors.newScheduledThreadPool(consumerCount);
        activeDirectoryConnector = new ActiveDirectoryConnector();
        for (int i = 0; i < consumerCount; ++i) {
            executorService.execute(new InMemoryQueueMessageConsumer(queue, k8sHandler, activeDirectoryConnector));
        }
    }

    @PreDestroy
    public void shutdown() {
        executorService.shutdown();
    }

    public void addToQueue(final JobCreationRequestConfig jobCreationRequestConfig) {
        queue.add(jobCreationRequestConfig);
    }

    public boolean isUserExists(final String identifer) {
        final String serviceName = identifer.split(HYPHEN)[0];
        final String userName = identifer.split(HYPHEN)[1];
        for (final JobCreationRequestConfig jobCreationRequestConfig : queue) {
            if (jobCreationRequestConfig.getUserName().equals(userName)
                    && jobCreationRequestConfig.getServiceName().equals(serviceName)) {
                return true;
            }
        }
        return false;
    }

}
