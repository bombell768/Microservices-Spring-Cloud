package com.example.personservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


@CrossOrigin(origins = "http://localhost:63342")
@RestController
public class PersonController {
    private PersonRepository dataRepo = new PersonRepositoryImpl();

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/persons/{id}")
    public EntityModel<Person> getPerson(@PathVariable int id) {
        System.out.println("...called GET for person with ID: " + id);
        Person thePerson = dataRepo.getPerson(id);
        EntityModel<Person> em = EntityModel.of(thePerson);


        // Add self-link
        em.add(linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel());

        // Add state-specific links
        switch (thePerson.getStatus()) {
            case ACTIVE:
                em.add(linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"));
                em.add(linkTo(methodOn(PersonController.class).hirePerson(id)).withRel("hire"));
                System.out.println("Adding hire link for ACTIVE status");
                em.add(linkTo(methodOn(PersonController.class).deactivatePerson(id)).withRel("deactivate"));
                System.out.println("Adding deactivate link for ACTIVE status");
                em.add(linkTo(methodOn(PersonController.class).encryptText(id)).withRel("encrypt"));
                break;
            case HIRED:
                em.add(linkTo(methodOn(PersonController.class).vacatePerson(id)).withRel("vacate"));
                System.out.println("Adding vacate link for HIRED status");
                break;
            case NOT_ACTIVE:
                em.add(linkTo(methodOn(PersonController.class).activatePerson(id)).withRel("activate"));
                System.out.println("Adding activate link for NOT_ACTIVE status");
                break;
        }

        // Add a link to list all persons
        em.add(linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all"));

        return em;
    }


    @GetMapping("/persons")
    public CollectionModel<EntityModel<Person>> getAllPersons() {
        System.out.println("...called GET");

        List<EntityModel<Person>> persons = dataRepo.getAllPersons().stream().map(person -> {
            EntityModel<Person> em = EntityModel.of(person);

            // Add self-link
            em.add(linkTo(methodOn(PersonController.class).getPerson(person.getId())).withSelfRel());

            // Add state-specific links
            switch (person.getStatus()) {
                case ACTIVE:
                    em.add(linkTo(methodOn(PersonController.class).deletePerson(person.getId())).withRel("delete"));
                    em.add(linkTo(methodOn(PersonController.class).hirePerson(person.getId())).withRel("hire"));
//                    System.out.println("Adding hire and deactivate links for ACTIVE status (ID: " + person.getId() + ")");
                    em.add(linkTo(methodOn(PersonController.class).deactivatePerson(person.getId())).withRel("deactivate"));
                    em.add(linkTo(methodOn(PersonController.class).encryptText(person.getId())).withRel("encrypt"));
                    break;
                case HIRED:
                    em.add(linkTo(methodOn(PersonController.class).vacatePerson(person.getId())).withRel("vacate"));
//                    System.out.println("Adding vacate link for HIRED status (ID: " + person.getId() + ")");
                    break;
                case NOT_ACTIVE:
                    em.add(linkTo(methodOn(PersonController.class).activatePerson(person.getId())).withRel("activate"));
//                    System.out.println("Adding activate link for NOT_ACTIVE status (ID: " + person.getId() + ")");
                    break;
            }

            // Add a link to list all persons
            em.add(linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all"));

            return em;
        }).collect(Collectors.toList());

        return CollectionModel.of(persons,
                linkTo(methodOn(PersonController.class).getAllPersons()).withSelfRel());
    }


    @DeleteMapping("/persons/{id}")
    public ResponseEntity<?> deletePerson(@PathVariable int id) {
        System.out.println("...called DELETE");
        dataRepo.deletePerson(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/persons/{id}")
    public EntityModel<Person> updatePerson(@PathVariable int id, @RequestBody Person person) throws PersonNotFoundEx {
        System.out.println("...called PUT");
        person.setId(id);
        return EntityModel.of(dataRepo.updatePerson(person),
                linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel(),
                linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"),
                linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all"));
    }

    @PostMapping("/persons")
    public EntityModel<Person> addPerson(@RequestBody Person person) throws BadRequestEx {
        System.out.println("...called POST");
        Person addedPerson = dataRepo.addPerson(person);

        return EntityModel.of(addedPerson,
                linkTo(methodOn(PersonController.class).getPerson(addedPerson.getId())).withSelfRel(),
                linkTo(methodOn(PersonController.class).deletePerson(addedPerson.getId())).withRel("delete"),
                linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all"));
    }

    @PatchMapping("/persons/{id}/hire")
    public ResponseEntity<?> hirePerson(@PathVariable int id) {
        Person thePerson = dataRepo.getPerson(id);

        if (thePerson.getStatus() == PersonStatus.ACTIVE) {
            thePerson.setStatus(PersonStatus.HIRED);
            dataRepo.updatePerson(thePerson);

            return ResponseEntity.ok()
                    .body(EntityModel.of(thePerson,
                            linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel(),
                            linkTo(methodOn(PersonController.class).vacatePerson(id)).withRel("vacate"),
                            linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all")));
        } else {
            throw new ConflictEx("You CAN'T hire a person with status " + thePerson.getStatus());
        }
    }

    @PatchMapping("/persons/{id}/vacate")
    public ResponseEntity<?> vacatePerson(@PathVariable int id) {
        Person thePerson = dataRepo.getPerson(id);

        if (thePerson.getStatus() != PersonStatus.HIRED) {
            throw new ConflictEx("You CAN'T vacate a person with status " + thePerson.getStatus());
        }

        thePerson.setStatus(PersonStatus.ACTIVE);
        dataRepo.updatePerson(thePerson);

        return ResponseEntity.ok()
                .body(EntityModel.of(thePerson,
                        linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel(),
                        linkTo(methodOn(PersonController.class).hirePerson(id)).withRel("hire"),
                        linkTo(methodOn(PersonController.class).deletePerson(id)).withRel("delete"),
                        linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all")));
    }

    @PatchMapping("/persons/{id}/activate")
    public ResponseEntity<?> activatePerson(@PathVariable int id) {
        System.out.println("PATCH activate person with id: " + id);
        Person person = dataRepo.getPerson(id);
        if (person.getStatus() == PersonStatus.NOT_ACTIVE) {
            person.setStatus(PersonStatus.ACTIVE);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(EntityModel.of(
                    person,
                    linkTo(methodOn(PersonController.class).hirePerson(person.getId()))
                            .withRel("hire"),
                    linkTo(methodOn(PersonController.class).deletePerson(person.getId()))
                            .withRel("delete"),
                    linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all")
            ), headers, HttpStatus.OK);
        } else {
            throw new ConflictEx("You can't activate a person with status " + person.getStatus());
        }
    }

    @PatchMapping("/persons/{id}/deactivate")
    public ResponseEntity<?> deactivatePerson(@PathVariable int id) {
        System.out.println("PATCH deactivate person with id: " + id);
        Person person = dataRepo.getPerson(id);

        if (person.getStatus() == PersonStatus.ACTIVE) {
            person.setStatus(PersonStatus.NOT_ACTIVE);
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            return new ResponseEntity<>(EntityModel.of(
                    person,
                    linkTo(methodOn(PersonController.class).hirePerson(person.getId()))
                            .withRel("hire"),
                    linkTo(methodOn(PersonController.class).deletePerson(person.getId()))
                            .withRel("delete"),
                    linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all")
            ), headers, HttpStatus.OK);
        } else {
            throw new ConflictEx("You can't deactivate a person with status " + person.getStatus());
        }
    }

    @GetMapping("/persons/{id}/encrypt")
    public ResponseEntity<?> encryptText(@PathVariable int id) {
        Person thePerson = dataRepo.getPerson(id);
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                "http://encrypt-service/encrypt",
                new String(dataRepo.getPerson(id).getText()), String.class
        );
        String response = responseEntity.getBody();
        thePerson.setText(response);  // Optionally update the person's text field with the encrypted text
        return ResponseEntity.ok()
                .body(EntityModel.of(thePerson,
                        linkTo(methodOn(PersonController.class).getPerson(id)).withSelfRel(),
                        linkTo(methodOn(PersonController.class).getAllPersons()).withRel("list all")));
    }
}
