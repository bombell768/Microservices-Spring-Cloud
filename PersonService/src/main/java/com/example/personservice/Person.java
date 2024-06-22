package com.example.personservice;

enum PersonStatus {
    ACTIVE,
    NOT_ACTIVE,
    HIRED
}

public class Person {
    private int id;
    private String firstName;
    private int age;
    private PersonStatus status;
    private String text;


    public Person() {
    }

    public Person(int id, String firstName, int age, String text) {
        this.id = id;
        this.firstName = firstName;
        this.age = age;
        this.status = PersonStatus.ACTIVE;
        this.text = text;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public PersonStatus getStatus() {
        return status;
    }

    public void setStatus(PersonStatus status) {
        this.status = status;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
