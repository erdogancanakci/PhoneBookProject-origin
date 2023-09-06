package com.example.application.person_information;

public class Person {
    private String name;
    private String lastName;
    private String street;
    private String city;
    private String country;
    private int phoneNumber;
    private String email;
    private final int id;
    private static int counter = 0;

    public Person () {
        this.id = counter++;
    }

    public Person(String name, String street, String lastName, String city, String country, int phoneNumber, String email) {
        this.name = name;
        this.street = street;
        this.lastName = lastName;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.id = counter++;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
