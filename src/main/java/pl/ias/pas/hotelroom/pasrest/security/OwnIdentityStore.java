package pl.ias.pas.hotelroom.pasrest.security;


import pl.ias.pas.hotelroom.pasrest.dao.UserDao;
import pl.ias.pas.hotelroom.pasrest.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.security.enterprise.credential.Credential;
import javax.security.enterprise.credential.UsernamePasswordCredential;
import javax.security.enterprise.identitystore.CredentialValidationResult;
import javax.security.enterprise.identitystore.IdentityStore;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@ApplicationScoped
public class OwnIdentityStore implements IdentityStore {

    @Inject
    private UserDao userDao;

    @Override
    public Set<String> getCallerGroups(CredentialValidationResult validationResult) {
        return IdentityStore.super.getCallerGroups(validationResult);
    }

    @Override
    public CredentialValidationResult validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential) {
            UsernamePasswordCredential upCredential = (UsernamePasswordCredential) credential;
            String username = upCredential.getCaller();
            String password = upCredential.getPasswordAsString();
            User user = userDao.isPasswordForUserCorrect(username, password);
            if (user != null) {
                HashSet<String> groups = new HashSet(Arrays.asList(user.getPermissionLevel()));
                return new CredentialValidationResult(user.getLogin(), groups);
            } else {
                return CredentialValidationResult.INVALID_RESULT;
            }
        } else {
            return CredentialValidationResult.NOT_VALIDATED_RESULT;
        }
    }
}
