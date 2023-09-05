package com.example.application.bookmanager;

import com.example.application.Person;

import java.util.HashMap;
import java.util.Map;

public class CrudMGR {
    public static final Map<Integer, Person> personMap = new HashMap<>(); //first field are corresponding to ID value
    public static final Map<Integer, Integer> personIDMap = new HashMap<Integer, Integer>(); //first field is id, second is PhoneNumber

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
