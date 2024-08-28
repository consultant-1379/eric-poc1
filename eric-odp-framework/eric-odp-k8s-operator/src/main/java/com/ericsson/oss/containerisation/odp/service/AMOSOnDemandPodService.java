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

import static com.ericsson.oss.containerisation.odp.util.Constants.COLON;
import static com.ericsson.oss.containerisation.odp.util.Constants.SEMI_COLON;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.jboss.logging.Logger;
import org.yaml.snakeyaml.Yaml;

import com.ericsson.nms.client.util.UserDetails;
import com.ericsson.oss.containerisation.odp.queue.JobCreationRequestConfig;
import com.ericsson.oss.containerisation.odp.util.Utils;

import io.fabric8.kubernetes.api.model.batch.v1.Job;

public class AMOSOnDemandPodService implements OnDemandPodService {

    private static final Logger LOGGER = Logger.getLogger(AMOSOnDemandPodService.class);

    private static final String AMOS_TEMPLATE_PATH = "amos-job-on-demand-template.yaml";

    @Override
    public Job loadTemplate(final JobCreationRequestConfig jobCreationRequestConfig, final UserDetails userDetails) {
	try (final InputStream templateStream = (AMOSOnDemandPodService.class.getClassLoader().getResourceAsStream(AMOS_TEMPLATE_PATH))) {
	    if (templateStream != null) {
		final Yaml yaml = new Yaml();
		final Map<String, Object> templateData = yaml.load(modifyTemplateWithValues(templateStream, jobCreationRequestConfig, userDetails));
		final Job job = yaml.loadAs(yaml.dump(templateData), Job.class);
		LOGGER.debugf("Created job from yaml template for user [%s]", jobCreationRequestConfig.getUserName());
		return job;
	    } else {
		throw new IOException("Unable to load the job template for user" + jobCreationRequestConfig.getUserName());
	    }
	} catch (final IOException ioException) {
	    LOGGER.error("IOException in loadTemplate for user [%s]", jobCreationRequestConfig.getUserName(), ioException);
	    return null;
	}

    }

    // This method replaces all the placeholders with actual values before building Job object
    private String modifyTemplateWithValues(final InputStream inputStream, final JobCreationRequestConfig jobCreationRequestConfig, final UserDetails userDetails)throws IOException {
	String templateInString = Utils.getYamlFileInString(inputStream);
	// Modify all Placeholders in the yaml template with actual values.
	templateInString = templateInString.replaceAll("<USER_VALUE>", jobCreationRequestConfig.getUserName());
	templateInString = templateInString.replaceAll("<SSO_TOKEN_VALUE>", jobCreationRequestConfig.getSsoToken());
	templateInString = templateInString.replaceAll("<MAIN_APPLICATION_USER_NAME_VALUE>", userDetails.getuId());
	templateInString = templateInString.replaceAll("<MAIN_APPLICATION_USER_UID_VALUE>", userDetails.getUidNumber().toString());
	templateInString = templateInString.replaceAll("<MAIN_APPLICATION_USER_PRIMARY_GID_VALUE>", userDetails.getgId());
	templateInString = templateInString.replaceAll("<INIT_GROUPS_VALUE>", getInitGroups(userDetails));
	templateInString = templateInString.replaceAll("<RUNASUSER_VALUE>", userDetails.getUidNumber().toString());
	templateInString = templateInString.replaceAll("<RUNASGROUP_VALUE>", userDetails.getgId());
	// TODO: Set timezone attribute with value from environment variable
	return templateInString;
    }

    private String getInitGroups(final UserDetails userDetails) {
	final StringBuilder stringBuilder = new StringBuilder();
	stringBuilder.append(userDetails.getInitGroups());
	userDetails.getSupplementaryGroups().forEach((key, value) -> stringBuilder.append(key).append(COLON).append(value).append(SEMI_COLON));
	final String initGroups = stringBuilder.toString();
	// To remove semicolon at the end.
	return initGroups.substring(0, initGroups.lastIndexOf(SEMI_COLON));
    }

}
