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
package com.ericsson.oss.containerisation.odp.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Utils {

    public static String getAppLabel(final String serviceName, final String userName) {
	return new StringBuilder().append(serviceName).append(Constants.HYPHEN).append(userName).toString();
    }

    // This method returns string from InputStream of on-demand job template.
    public static String getYamlFileInString(final InputStream inputStream) throws IOException {
	final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	int nRead;
	final byte[] data = new byte[1024];
	while ((nRead = inputStream.read(data, 0, data.length)) != -1) {
	    byteArrayOutputStream.write(data, 0, nRead);
	}

	byteArrayOutputStream.flush();
	final byte[] byteArray = byteArrayOutputStream.toByteArray();
	return new String(byteArray, StandardCharsets.UTF_8);
    }

    // As we use userName in the name of the job to be created, ensure underscore in the name is replaced by hyphen.
    // Otherwise, K8s will complain and not create Job.Also, append a hashcode so that it is possible to have Job created for users
    // like amos-user and amos_user.
    public static String getUpdatedJobName(final String jobName) {
	return new StringBuilder().append(jobName.replaceAll(Constants.UNDERSCORE, Constants.HYPHEN)).append(Constants.HYPHEN)
		.append(jobName.hashCode()).toString();
    }

}
