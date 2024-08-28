package com.ericsson.oss.odp.sso;

import com.ericsson.oss.api.PamOpenAmApi;
import com.ericsson.oss.invoker.ApiClient;
import com.ericsson.oss.invoker.ApiException;
import com.ericsson.oss.model.TokenOrMessageResponse;
import com.ericsson.oss.model.TokenValidationResponse;

import javax.ws.rs.core.MediaType;

public class PamOpenAmApiWithDefaults extends PamOpenAmApi {

    public static final String DEFAULT_HEADER_PARAM_IN_ACCEPT = MediaType.TEXT_PLAIN;

    public PamOpenAmApiWithDefaults() {
        super();
    }

    public PamOpenAmApiWithDefaults(final ApiClient apiClient) {
        super(apiClient);
    }

    public TokenOrMessageResponse pamAuthenticate(final String xOpenAMUsername, final String tokenId) throws ApiException {
        return this.pamAuthenticate(DEFAULT_HEADER_PARAM_IN_ACCEPT, xOpenAMUsername, tokenId);
    }

    public TokenValidationResponse pamValidate(final String xOpenAMUsername, final String tokenId) throws ApiException {
        return this.pamValidate(DEFAULT_HEADER_PARAM_IN_ACCEPT, xOpenAMUsername, tokenId);
    }
}
