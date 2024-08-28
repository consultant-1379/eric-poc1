/*------------------------------------------------------------------------------
 *******************************************************************************
 * COPYRIGHT Ericsson 2019
 *
 * The copyright to the computer program(s) herein is the property of
 * Ericsson Inc. The programs may be used and/or copied only with written
 * permission from Ericsson Inc. or in accordance with the terms and
 * conditions stipulated in the agreement/contract under which the
 * program(s) have been supplied.
 *******************************************************************************
 *----------------------------------------------------------------------------*/
package com.ericsson.nms.client.config;

import java.io.IOException;

/**
 * Configuration reader for ENIQ-S WAS (Windows Application Server) Integration
 *
 * @author ekarpia
 *
 */
public class TlsPropertiesReader extends ApplicationPropertiesReader {

    public static final String TLS_CONFIGURATION = "/ericsson/tor/data/tls.properties";
    public static final String CIPHERS_PROPERTY_KEY = "ciphers";

    public static final String WEAK_CIPHERS_DATA = "/ericsson/tor/data/weak-ciphers-list.txt";
    public static final String WEAK_CIPHERS_PROPERTY_KEY = "weakCiphers";

    /**
     * @throws IOException
     */
    public TlsPropertiesReader(String propertyType) throws IOException {
        super(propertyType);
    }

}
