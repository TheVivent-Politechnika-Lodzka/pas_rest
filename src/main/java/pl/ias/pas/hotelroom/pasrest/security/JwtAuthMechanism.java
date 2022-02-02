package pl.ias.pas.hotelroom.pasrest.security;

import com.nimbusds.jwt.SignedJWT;

import javax.enterprise.context.ApplicationScoped;
import javax.security.enterprise.AuthenticationException;
import javax.security.enterprise.AuthenticationStatus;
import javax.security.enterprise.authentication.mechanism.http.HttpAuthenticationMechanism;
import javax.security.enterprise.authentication.mechanism.http.HttpMessageContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

@ApplicationScoped
public class JwtAuthMechanism implements HttpAuthenticationMechanism {

    public final static String AUTHORIZATION_HEADER = "Authorization";
    public final static String BEARER_PREFIX = "Bearer ";

    @Override
    public AuthenticationStatus validateRequest(
            HttpServletRequest request,
            HttpServletResponse response,
            HttpMessageContext context)
            throws AuthenticationException {


        System.out.println("JwtAuthMechanism.validateRequest()");

        if (request.getRequestURL().toString().endsWith("login") || request.getRequestURL().toString().endsWith("test")) {
            return context.doNothing();
        }

        String authorizationHeader = request.getHeader(AUTHORIZATION_HEADER);
        if (authorizationHeader == null || !authorizationHeader.startsWith(BEARER_PREFIX)) {
            System.out.println("No authorization header");
            return context.responseUnauthorized();
        }

        String token = authorizationHeader.substring(BEARER_PREFIX.length()).trim();

        if (JwtUtils.validateJwtToken(token)) {
            try {
                SignedJWT signedJWT = SignedJWT.parse(token);
                String login = signedJWT.getJWTClaimsSet().getSubject();
                String groups = signedJWT.getJWTClaimsSet().getStringClaim("permissionLevel");
                Date expirationDate = (Date) signedJWT.getJWTClaimsSet().getClaim("exp");
                boolean expired = new Date().after(expirationDate);
                if (expired) {
                    System.out.println("Token expired");
                    return context.responseUnauthorized();
                }

                HashSet<String> roles = new HashSet<>(Arrays.asList(groups.split(",")));
                return context.notifyContainerAboutLogin(login, roles);


            } catch (Exception e) {
                e.printStackTrace();
                return context.responseUnauthorized();
            }
        }

        System.out.println("WTF");
        return context.responseUnauthorized();
    }
}
