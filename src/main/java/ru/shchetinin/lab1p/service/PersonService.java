package ru.shchetinin.lab1p.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import ru.shchetinin.lab1p.dao.PersonDao;
import ru.shchetinin.lab1p.entity.Person;
import ru.shchetinin.lab1p.excepion.EndpointException;

import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class PersonService {

    @Inject
    private PersonDao personDao;

    @Transactional
    public Person createPerson(Person person) {
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

    public List<Person> getAllPersons() {
        return personDao.getAll();
    }

    @Transactional
    public Person updatePerson(Long id, Person updatedPerson) {
        Optional<Person> personOptional = personDao.findById(id);
        if (personOptional.isEmpty()) {
            throw new EndpointException(Response.Status.NOT_FOUND, String.format("Person with id %d not found", id));
        }
        var person = personOptional.get();
        person.setName(updatedPerson.getName());
        person.setEyeColor(updatedPerson.getEyeColor());
        person.setHairColor(updatedPerson.getHairColor());
        person.setLocation(updatedPerson.getLocation());
        person.setBirthday(updatedPerson.getBirthday());
        person.setNationality(updatedPerson.getNationality());
        personDao.update(person);
        return person;
    }

    @Transactional
    public void deletePerson(Long id) {
        if (!personDao.delete(id)) {
            throw new EndpointException(Response.Status.NOT_FOUND, String.format("Person with id %d not found", id));
        }
    }
}
