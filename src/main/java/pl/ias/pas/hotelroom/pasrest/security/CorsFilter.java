package pl.ias.pas.hotelroom.pasrest.security;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.ext.Provider;
import java.io.IOException;

@Provider
@PreMatching
public class CorsFilter implements ContainerResponseFilter {
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext) {
        String origin = requestContext.getHeaderString("Origin");
        if ((origin != null)
                && origin.startsWith("http://localhost") ) {
            allowExceptionCors(requestContext, responseContext, origin);
        }
    }

    private void allowExceptionCors(ContainerRequestContext requestContext, ContainerResponseContext responseContext, String origin) {
        String methodHeader = requestContext.getHeaderString("Access-Control-Request-Method");
        String requestHeaders = requestContext.getHeaderString("Access-Control-Request-Headers");

        MultivaluedMap<String, Object> headers = responseContext.getHeaders();
        headers.putSingle("Access-Control-Allow-Origin", origin);
        headers.putSingle("Access-Control-Allow-Credentials", "true");
        headers.putSingle("Access-Control-Allow-Methods", methodHeader);
        headers.putSingle("Access-Control-Allow-Headers", "x-requested-with," + (requestHeaders == null ? "" : requestHeaders));
    }
}
