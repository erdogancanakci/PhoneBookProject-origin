package com.example.application.storage;

import com.example.application.data.Person;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PersonDataStorage {
    private static final Map<Integer, Person> personIDtoPerson = new ConcurrentHashMap<>();
    private static final Map<Integer, Integer> personIDtoPersonPhone = new ConcurrentHashMap<>();
    public static Map<Integer, Person> getPersonIDtoPerson() {
        return personIDtoPerson;
    }
    public static Map<Integer, Integer> getPersonIDtoPersonPhone() {
        return personIDtoPersonPhone;
    }

}
