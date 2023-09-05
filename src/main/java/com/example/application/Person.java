package com.example.application;

import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

public class Person {
    private static HashSet<Integer> hashSet = new HashSet<>();
    private String name;
    private String lastName;
    private String street;
    private String city;
    private String country;
    private int phoneNumber;
    private String email;
    private int id;

    public Person () {
        this.id = createRandom();  //UUID or static counter can be used instead createRandom() method
    }

    public Person(String name, String street, String lastName, String city, String country, int phoneNumber, String email) {
        this.name = name;
        this.street = street;
        this.lastName = lastName;
        this.city = city;
        this.country = country;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.id = createRandom();

    }

    private static int createRandom() {
        Random random = new Random();
        int data = random.nextInt(Integer.MAX_VALUE);

        if(hashSet.add(data))
            return data;
        else
            return createRandom();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        return phoneNumber == person.phoneNumber;
    }

    @Override
    public int hashCode() {
        return phoneNumber;
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
