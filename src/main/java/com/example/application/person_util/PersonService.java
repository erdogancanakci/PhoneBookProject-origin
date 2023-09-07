package com.example.application.person_util;

import com.example.application.storage.PersonDataStorage;
import com.example.application.data.Person;
import java.util.ArrayList;
import java.util.List;

public class PersonService {
    private PersonService () {
    }

    public static List<Person> getPersonAsSublist(int page, int pageSize) {
        List<Person> allPersons = new ArrayList<>(PersonDataStorage.getPersonIDtoPerson().values());
        return allPersons.subList(page * pageSize, Math.min(page * pageSize + pageSize, allPersons.size()));
    }

    public static int getCount() {
        return new ArrayList<>(PersonDataStorage.getPersonIDtoPerson().values()).size();
    }

}
