package com.example.application.repository;

import com.example.application.data.Person;

import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PersonDataRepository {
    private static final Map<Integer, Person> idToPersonMap = new ConcurrentHashMap<>();
    private static final Map<Integer, Integer> idToPhoneMap = new ConcurrentHashMap<>();
    private static final HashSet<Integer> phoneNumberSet = new HashSet<>();

    public static HashSet<Integer> getPhoneNumberSet() {
        return phoneNumberSet;
    }
    public static Map<Integer, Integer> getIdToPhoneMap() {
        return idToPhoneMap;
    }
    public static Map<Integer, Person> getIdToPersonMap() {
        return idToPersonMap;
    }
}
