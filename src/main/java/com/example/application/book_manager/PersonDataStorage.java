package com.example.application.book_manager;

import com.example.application.person_information.Person;

import java.util.HashMap;
import java.util.Map;

public class PersonDataStorage {
    public static final Map<Integer, Person> personMap = new HashMap<>(); //first field are corresponding to ID value
    public static final Map<Integer, Integer> personIDMap = new HashMap<Integer, Integer>(); //first field is id, second is PhoneNumber

}
