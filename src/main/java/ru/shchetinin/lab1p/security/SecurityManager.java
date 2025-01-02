package ru.shchetinin.lab1p.security;

import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import ru.shchetinin.lab1p.excepion.EndpointException;

import java.util.Objects;
import java.util.Set;

import static jakarta.ws.rs.core.Response.Status.FORBIDDEN;
import static ru.shchetinin.lab1p.entity.Role.ADMIN;
import static ru.shchetinin.lab1p.entity.Role.USER;

@RequestScoped
public class SecurityManager {

    @Inject
    private AuthenticatedCaller authenticatedCaller;

    public boolean hasAnyRole(String... roles) {
        Set<String> callerRoles = authenticatedCaller.getRoles();

        if (callerRoles == null) {
            return false;
        }

        for (String role : roles) {
            if (callerRoles.contains(role)) {
                return true;
            }
        }

        return false;
    }

    public boolean isAnonymous() {
        return !authenticatedCaller.isAuthenticated();
    }

    public void throwIfAnonymous() {
        if (isAnonymous()) {
            throwForbiddenException();
        }
    }

    public Long getCallerPrincipal() {
        return authenticatedCaller.getId();
    }

    public void throwIfUserHasNotAccessToResource(Long resourceOwner) {
        if (hasAnyRole(String.valueOf(ADMIN))) {
            return;
        }

        boolean isUserHasAccess = hasAnyRole(String.valueOf(USER)) &&
                Objects.equals(resourceOwner, getCallerPrincipal());

        if (!isUserHasAccess) {
            throwForbiddenException();
        }
    }

    public void throwForbiddenException() {
        throw new EndpointException(
                FORBIDDEN,
                "You do not have permission to access this resource"
        );
    }
}
