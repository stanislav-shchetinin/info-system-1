package ru.shchetinin.lab1p.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.modelmapper.ModelMapper;
import ru.shchetinin.lab1p.dao.PersonDao;
import ru.shchetinin.lab1p.dao.UserDao;
import ru.shchetinin.lab1p.dto.request.PersonRequest;
import ru.shchetinin.lab1p.entity.Location;
import ru.shchetinin.lab1p.entity.Person;
import ru.shchetinin.lab1p.entity.Role;
import ru.shchetinin.lab1p.excepion.EndpointException;
import ru.shchetinin.lab1p.security.SecurityManager;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@ApplicationScoped
public class PersonService {

    @Inject
    private PersonDao personDao;

    @Inject
    private UserDao userDao;

    @Inject
    private ModelMapper modelMapper;

    @Inject
    private LocationService locationService;

    @Inject
    private SecurityManager securityManager;


    @Transactional
    public Person createPerson(PersonRequest personRequest, String username) {
        Person person = modelMapper.map(personRequest, Person.class);

        setDependencies(personRequest, person);
        var opt = userDao.findByUsername(username);
        opt.ifPresent(user -> person.setCreator(user.getId()));

        personDao.save(person);

        return person;
    }

    public Person getPersonById(Long id) {
        Optional<Person> person = personDao.findById(id);
        if (person.isEmpty()) {
            throw new EndpointException(Response.Status.NOT_FOUND, String.format("Person with id %d not found", id));
        }
        return person.get();
    }

    public List<Person> getAllPersons(int page, int size) {
        return personDao.getAll(page, size);
    }

    @Transactional
    public Person updatePerson(Long id, PersonRequest personRequest, String username) {

        var opt = userDao.findByUsername(username);
        boolean isAuth = false;

        if (opt.isPresent()) {
            var creator = personDao.findById(id).get().getCreator();
            isAuth = Objects.equals(creator, opt.get().getId()) || opt.get().getRole().equals(Role.ADMIN);
        }

        if (!isAuth) {
            throw new EndpointException(Response.Status.FORBIDDEN, String.format("Person with id %d is not creator", id));
        }

        Optional<Person> personOptional = personDao.findById(id);
        if (personOptional.isEmpty()) {
            throw new EndpointException(Response.Status.NOT_FOUND, String.format("Person with id %d not found", id));
        }
        var person = personOptional.get();
        modelMapper.map(personRequest, person);

        setDependencies(personRequest, person);

        personDao.update(person);

        return person;
    }

    @Transactional
    public void deletePerson(Long id, String username) {
        var opt = userDao.findByUsername(username);
        boolean isAuth = false;

        if (opt.isPresent()) {
            var creator = personDao.findById(id).get().getCreator();
            isAuth = Objects.equals(creator, opt.get().getId()) || opt.get().getRole().equals(Role.ADMIN);
        }

        if (!isAuth) {
            throw new EndpointException(Response.Status.FORBIDDEN, String.format("Person with id %d is not creator", id));
        }

        if (!personDao.delete(id)) {
            throw new EndpointException(Response.Status.NOT_FOUND, String.format("Person with id %d not found", id));
        }
    }

    private void setDependencies(PersonRequest personRequest, Person person) {
        if (personRequest.getLocation() != null) {
            Location location = modelMapper.map(
                    locationService.getLocationById(personRequest.getLocation()),
                    Location.class
            );
            person.setLocation(location);
        }
    }

}
