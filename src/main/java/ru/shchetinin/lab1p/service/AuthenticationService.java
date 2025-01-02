package ru.shchetinin.lab1p.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import ru.shchetinin.lab1p.dao.UserDao;
import ru.shchetinin.lab1p.dto.request.AuthenticationRequest;
import ru.shchetinin.lab1p.dto.response.AuthenticationResponse;
import ru.shchetinin.lab1p.entity.User;
import ru.shchetinin.lab1p.excepion.EndpointException;
import ru.shchetinin.lab1p.security.JwtUtil;
import ru.shchetinin.lab1p.security.PasswordHash;

import java.util.Optional;

import static jakarta.ws.rs.core.Response.Status.UNAUTHORIZED;

@ApplicationScoped
public class AuthenticationService {

    @Inject
    private UserDao userDAO;

    @Inject
    private PasswordHash passwordHash;

    @Inject
    private JwtUtil jwtUtil;

    @Transactional
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        String username = request.getUsername();

        Optional<User> optionalUser = userDAO.findByUsername(username);

        if (optionalUser.isEmpty()) {
            throw new EndpointException(UNAUTHORIZED, "Incorrect username or password");
        }

        User user = optionalUser.get();

        boolean verified = passwordHash.verify(
                request.getPassword(),
                user.getPassword()
        );

        if (!verified) {
            throw new EndpointException(UNAUTHORIZED, "Incorrect username or password");
        }

        String accessToken = jwtUtil.generateAccessToken(username);

        return new AuthenticationResponse(
                accessToken,
                String.valueOf(user.getRole()),
                user.getUsername()
        );
    }

}
