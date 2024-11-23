package ru.shchetinin.lab1p.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import org.modelmapper.ModelMapper;
import ru.shchetinin.lab1p.dao.PersonDao;
import ru.shchetinin.lab1p.dto.request.PersonRequest;
import ru.shchetinin.lab1p.entity.Location;
import ru.shchetinin.lab1p.entity.Person;
import ru.shchetinin.lab1p.excepion.EndpointException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PersonService {

    @Inject
    private PersonDao personDao;

    @Inject
    private ModelMapper modelMapper;

    @Inject
    private LocationService locationService;


    @Transactional
    public Person createPerson(PersonRequest personRequest) {
        Person person = modelMapper.map(personRequest, Person.class);

        setDependencies(personRequest, person);

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
    public Person updatePerson(Long id, PersonRequest personRequest) {
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
    public void deletePerson(Long id) {
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
