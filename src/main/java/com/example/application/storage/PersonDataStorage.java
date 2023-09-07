package com.example.application.storage;

import com.example.application.data.Person;

import java.util.HashMap;
import java.util.Map;

public class PersonDataStorage {
    private static final Map<Integer, Person> personIDtoPerson = new HashMap<>();
    private static final Map<Integer, Integer> personIDtoPersonPhone = new HashMap<Integer, Integer>();
    public static Map<Integer, Person> getPersonIDtoPerson() {
        return personIDtoPerson;
    }
    public static Map<Integer, Integer> getPersonIDtoPersonPhone() {
        return personIDtoPersonPhone;
    }

}
