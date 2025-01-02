package ru.shchetinin.lab1p.security;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.security.enterprise.credential.Credential;
import jakarta.security.enterprise.credential.UsernamePasswordCredential;
import ru.shchetinin.lab1p.dao.UserDao;
import ru.shchetinin.lab1p.entity.User;

import java.util.Optional;
import java.util.Set;

@ApplicationScoped
public class PostgresIdentityStoreHandler {

    @Inject
    private UserDao userDAO;

    @Inject
    private AuthenticatedCaller authenticatedCaller;

    public AuthenticatedCaller validate(Credential credential) {
        if (credential instanceof UsernamePasswordCredential upc) {
            String username = upc.getCaller();

            Optional<User> optionalUser = userDAO.findByUsername(username);

            if (optionalUser.isEmpty()) {

                return authenticatedCaller;
            }

            User user = optionalUser.get();

            Set<String> roles = Set.of(String.valueOf(user.getRole()));

            initCaller(authenticatedCaller, user, roles);

            return authenticatedCaller;
        }



        return authenticatedCaller;
    }

    private void initCaller(AuthenticatedCaller authenticatedCaller, User user, Set<String> roles) {
        authenticatedCaller.setId(user.getId());
        authenticatedCaller.setAuthenticated(true);
        authenticatedCaller.setRoles(roles);
    }
}

