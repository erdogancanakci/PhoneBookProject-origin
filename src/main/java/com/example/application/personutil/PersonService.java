package com.example.application.personutil;

import com.example.application.storage.PersonDataStorage;

import java.util.ArrayList;

public class PersonService {
    private PersonService () {
    }

    public static int getPersonIDToPersonMapSize() {
        return new ArrayList<>(PersonDataStorage.getIDtoPersonMap().values()).size();
    }

}
