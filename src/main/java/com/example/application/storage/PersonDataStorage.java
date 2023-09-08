package com.example.application.storage;

import com.example.application.data.Person;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PersonDataStorage {
    private static final Map<Integer, Person> IDtoPerson = new ConcurrentHashMap<>();
    private static final Map<Integer, Integer> IDtoPhone = new ConcurrentHashMap<>();
    public static Map<Integer, Person> getIDtoPersonMap() {
        return IDtoPerson;
    }
    public static Map<Integer, Integer> getIDtoPhoneMap() {
        return IDtoPhone;
    }

}
