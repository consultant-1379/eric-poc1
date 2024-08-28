package com.ericsson.oss.odp.sso;

import com.ericsson.oss.model.TokenOrMessageResponse;
import com.ericsson.oss.model.TokenValidationResponse;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJacksonProvider;

import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.Provider;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

@Provider
@Produces({"text/plain"})
@Consumes({"text/plain"})
public class PamOpenAmTextPlainToJsonProvider extends ResteasyJacksonProvider {

    public boolean isReadable(final Class<?> aClass, final Type type, final Annotation[] annotations, final MediaType mediaType) {
        return aClass.getName().equals(TokenOrMessageResponse.class.getName()) ||
                aClass.getName().equals(TokenValidationResponse.class.getName());
    }

}
