package ru.shchetinin.lab1p.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import ru.shchetinin.lab1p.dao.ApplicationDAO;
import ru.shchetinin.lab1p.dao.UserDao;
import ru.shchetinin.lab1p.dto.response.ApplicationDTO;
import ru.shchetinin.lab1p.entity.Application;
import ru.shchetinin.lab1p.entity.User;
import ru.shchetinin.lab1p.excepion.EndpointException;
import ru.shchetinin.lab1p.security.SecurityManager;

import java.util.List;
import java.util.Optional;

import static jakarta.ws.rs.core.Response.Status.NOT_FOUND;

@ApplicationScoped
public class AdminService {

    @Inject
    private ApplicationDAO applicationDAO;

    @Inject
    private UserDao userDAO;

    @Inject
    private SecurityManager securityManager;

    @Inject
    private ModelMapper modelMapper;


    @Transactional
    public void submitApplication(String username) {
        if (!userDAO.findAdmins().isEmpty()) {
            var app = new Application();
            app.setCreator(userDAO.findByUsername(username).get().getId());
            applicationDAO.save(app);
            return;
        }

        Long callerPrincipal = securityManager.getCallerPrincipal();

        setAdminRole(callerPrincipal);
    }

    @Transactional
    public void acceptApplication(ApplicationDTO applicationDTO) {
        if (applicationDTO == null) return;
        Optional<Application> application = applicationDAO.findById(applicationDTO.getId());

        if (application.isEmpty()) {
            throw new EndpointException(
                    NOT_FOUND,
                    "Application with id " + applicationDTO.getId() + " not found"
            );
        }

        Long applicationOwner = application.get()
                .getCreator();

        setAdminRole(applicationOwner);

        applicationDAO.delete(application.get().getId());
    }

    public List<ApplicationDTO> getAllApplications(int page, int size) {
        return applicationDAO.getAll(page, size)
                .stream()
                .map(this::convertToApplicationDTO)
                .toList();
    }

    private ApplicationDTO convertToApplicationDTO(Application application) {
        User user = userDAO.findById(application.getCreator()).get();
        ApplicationDTO map = modelMapper.map(application, ApplicationDTO.class);
        map.setUsername(user.getUsername());

        return map;
    }

    @Transactional
    private void setAdminRole(Long userId) {
        userDAO.setAdminRole(userId);
    }
}
