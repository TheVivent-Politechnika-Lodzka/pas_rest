package pl.ias.pas.hotelroom.pasrest.endpoints;

import pl.ias.pas.hotelroom.pasrest.model.CredentialsData;
import pl.ias.pas.hotelroom.pasrest.security.JwtUtils;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.Password;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStoreHandler;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

@RequestScoped
@Path("/auth")
public class AuthEndpoint {

    @Inject
    private IdentityStoreHandler indentityStore;

    @GET
    @Path("/test")
    public String test() {
        return "test";
    }

    @POST
    @Consumes("application/json")
    @Produces("text/plain")
    @Path("/login")
    public Response auth(@NotNull CredentialsData credentials) {
        Credential credential = new UsernamePasswordCredential(credentials.getLogin(), new Password(credentials.getPassword()));
        CredentialValidationResult result = indentityStore.validate(credential);

        if (result.getStatus() == CredentialValidationResult.Status.VALID) {
            return Response.accepted()
                    .type("application/jwt")
                    .entity(JwtUtils.generateJwtString(result))
                    .build();
        }

        return Response.status(Response.Status.UNAUTHORIZED).build();

    }

}
