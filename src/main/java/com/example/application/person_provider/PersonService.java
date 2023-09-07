package com.example.application.person_provider;

import com.example.application.book_manager.PersonDataStorage;
import com.example.application.person_information.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonService {

    public static List<Person> getPersonAsSublist(int page, int pageSize) {
        List<Person> allPersons = new ArrayList<>(PersonDataStorage.getPersonMap().values());
        return allPersons.subList(page * pageSize, Math.min(page * pageSize + pageSize, allPersons.size()));
    }

    public static int getCount() {
        return new ArrayList<>(PersonDataStorage.getPersonMap().values()).size();
    }
}
