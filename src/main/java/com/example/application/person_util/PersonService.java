package com.example.application.person_util;

import com.example.application.storage.PersonDataStorage;
import com.example.application.data.Person;
import java.util.ArrayList;
import java.util.List;

public class PersonService {
    private PersonService () {
    }

    public static int getPersonIDToPersonSize() {
        return new ArrayList<>(PersonDataStorage.getPersonIDtoPerson().values()).size();
    }

}
