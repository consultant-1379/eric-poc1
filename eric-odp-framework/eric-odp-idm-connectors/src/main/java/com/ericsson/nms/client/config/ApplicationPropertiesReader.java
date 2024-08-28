/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2017
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.nms.client.config;

import java.io.*;
import java.util.Properties;

/**
 *
 * Default implementation for external configuration readers
 *
 * @author ekarpia
 */
public class ApplicationPropertiesReader {

    private final String deploymentConfigLocation;

    private Properties props;

    /**
     * Prevent implementations from creating empty deployment reader
     */
    private ApplicationPropertiesReader() {
        this.deploymentConfigLocation = null;
    }

    /**
     * Load all global properties
     *
     * @throws IOException
     */
    public ApplicationPropertiesReader(final String deploymentConfigLocation) throws IOException {

        this.deploymentConfigLocation = deploymentConfigLocation;
        this.getDeploymentProperties();

    }

    /**
     * @throws IOException
     *
     */
    protected final void getDeploymentProperties() throws IOException {

        if (new File(this.deploymentConfigLocation).exists()) {
            try (final BufferedReader reader = getBufferedReader();) {
                this.props = new Properties();
                this.props.load(reader);
            } catch (final IOException e) {
                throw new IOException(e);
            }
        }
    }

    /**
     *
     * @param property
     * @return
     * @throws IOException
     */
    public final String getValue(final String property) throws IOException {

        if (this.props == null) {
            throw new IOException("Properties are not available.");
        }

        if (this.props.containsKey(property)) {
            return this.props.get(property).toString();
        } else {
            throw new IOException("Property " + property + " does not exist");
        }

    }

    private final BufferedReader getBufferedReader() throws FileNotFoundException {
        return new BufferedReader(new FileReader(this.deploymentConfigLocation));
    }
}
