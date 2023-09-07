package com.example.application.book_manager;

import com.example.application.person_information.Person;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PersonDataStorage {
    private static final Map<Integer, Person> personMap = new HashMap<>();
    private static final Map<Integer, Integer> personIDMap = new HashMap<Integer, Integer>();

    public static Map<Integer, Person> getPersonMap() {
        return personMap;
    }

    public static Map<Integer, Integer> getPersonIDMap() {
        return personIDMap;
    }



}
