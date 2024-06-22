package com.example.personservice;

import java.util.ArrayList;
import java.util.List;

public class PersonRepositoryImpl implements PersonRepository {
    private List<Person> personList;

    public PersonRepositoryImpl() {
        personList = new ArrayList<>();
        personList.add(new Person(1, "Mariusz" , 50, "Mały Książę"));
        personList.add(new Person(2, "Andrzej", 10, "aaaaaaaaaaaaaaaa"));
    }

    public List<Person> getAllPersons() {
        return personList;
    }

    public Person getPerson(int id) throws PersonNotFoundEx {
        for (Person thePerson : personList) {
            if (thePerson.getId() == id) {
                return thePerson;
            }
        }
        throw new PersonNotFoundEx(id);
    }

    public Person updatePerson(Person person) throws PersonNotFoundEx {
        for (Person thePerson : personList) {
            if (thePerson.getId() == person.getId()) {
                thePerson.setFirstName(person.getFirstName());
                thePerson.setAge(person.getAge());
                thePerson.setStatus(person.getStatus());
                return thePerson;
            }
        }
        throw new PersonNotFoundEx(person.getId());
    }

    public boolean deletePerson(int id) throws PersonNotFoundEx {
        for (Person thePerson : personList) {
            if (thePerson.getId() == id) {
                personList.remove(thePerson);
                return true;
            }
        }
        throw new PersonNotFoundEx(id);
    }

    public Person addPerson(Person person) throws BadRequestEx {
        for (Person thePerson : personList) {
            if (thePerson.getId() == person.getId()) {
                throw new BadRequestEx(thePerson.getId());
            }
        }
        personList.add(person);
        return person;
    }
}
