package net.amahdy.vaadin.demo.grid.util;

public class Person {
    String firstName;
    String lastName;
    String sotu;
    Integer id;

    public Person(int id, String firstName, String lastName, String sotu) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sotu = sotu;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getSotu() {
        return sotu;
    }

    public void setSotu(String sotu) {
        this.sotu = sotu;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
}
