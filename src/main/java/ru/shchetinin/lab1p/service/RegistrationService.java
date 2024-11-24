package ru.shchetinin.lab1p.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import ru.shchetinin.lab1p.config.PasswordHasher;
import ru.shchetinin.lab1p.dao.UserDao;
import ru.shchetinin.lab1p.dto.request.UserRequest;
import ru.shchetinin.lab1p.entity.Role;
import ru.shchetinin.lab1p.entity.User;
import ru.shchetinin.lab1p.excepion.EndpointException;

import static jakarta.ws.rs.core.Response.Status.CONFLICT;

@ApplicationScoped
public class RegistrationService {

    @Inject
    private UserDao userDao;

    @Inject
    private ModelMapper mapper;


    @Transactional
    public User register(UserRequest request) {
        String username = request.getUsername();

        if (userDao.isUserExist(username)) {
            throw new EndpointException(CONFLICT, "User with that name already exists");
        }

        User user = mapper.map(request, User.class);
        user.setPassword(PasswordHasher.hashPassword(user.getPassword()));
        user.setRole(Role.USER);

        userDao.save(user);

        return user;
    }


}
