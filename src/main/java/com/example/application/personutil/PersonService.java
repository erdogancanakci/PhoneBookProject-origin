package com.example.application.personutil;

import com.example.application.repository.PersonDataRepository;
import java.util.ArrayList;

public class PersonService {
    private PersonService () {
    }

    public static int getPersonIDToPersonMapSize() {
        return new ArrayList<>(PersonDataRepository.getIdToPersonMap().values()).size();
    }

}
