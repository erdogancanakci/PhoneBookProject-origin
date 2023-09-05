package com.example.application.book_manager;

import com.example.application.person_information.Person;

import java.util.HashMap;
import java.util.Map;

import static com.example.application.book_manager.PersonDataStorage.personIDMap;
import static com.example.application.book_manager.PersonDataStorage.personMap;

public class PhoneBookManager {

    public static synchronized void addPerson(Person item) {
        if(!personIDMap.containsKey(item.getId())) {
            if(!personIDMap.containsValue(item.getPhoneNumber())) {
                personMap.put(item.getId(), item);
                personIDMap.put(item.getId(), item.getPhoneNumber());
            }
        }
        else {
            int oldNumber = personIDMap.get(item.getId());
            if(!personIDMap.containsValue(item.getPhoneNumber())) {
                personIDMap.replace(item.getId(), oldNumber, item.getPhoneNumber());
                item.setPhoneNumber(item.getPhoneNumber());
            }
            else {
                personIDMap.replace(item.getId(), oldNumber, oldNumber);
                item.setPhoneNumber(oldNumber);
            }
        }
    }

    public static synchronized void deletePerson(Person item) {
        personMap.remove(item.getId(), item);
        personIDMap.remove(item.getId());
    }
}
