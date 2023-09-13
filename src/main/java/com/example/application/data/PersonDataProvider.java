package com.example.application.data;

import com.example.application.manager.DBPersonManager;
import com.example.application.repository.PersonDataRepository;

import java.sql.SQLException;
import java.util.Random;

public class PersonDataProvider {
    private static final int PERSON_COUNT = 10;
    private static final String ALPHABET_EN = "abcdefghijklmnopqrstuwxvyz";
    private static PersonDataProvider personDataProvider;
    private static final DBPersonManager DB_PERSON_MANAGER = new DBPersonManager();
    private PersonDataProvider() {
    }

    public static PersonDataProvider getPersonDataProvider() {
        if(personDataProvider == null) {
            personDataProvider = new PersonDataProvider();
            createRandomPerson();
        }
        return personDataProvider;
    }

    private static void createRandomPerson() {
        createRandomPerson(PERSON_COUNT);
    }

    private static void createRandomPerson(int count) {
        for(int i = 0; i < count; i++) {
            Person person = new Person();
            person.setName(getRandomText());
            person.setLastName(getRandomText());
            person.setEmail(getRandomText(10) +"@" +getRandomText(5) + ".com");
            person.setStreet(getRandomText());
            person.setCity(getRandomText());
            person.setPhoneNumber(getRandomNumber());
            person.setCountry(getRandomText());

            if(PersonDataRepository.getPhoneNumberSet().add(person.getPhoneNumber())) {
                PersonDataRepository.getIdToPersonMap().put(person.getId(), person);
                PersonDataRepository.getIdToPhoneMap().put(person.getId(), person.getPhoneNumber());

                DB_PERSON_MANAGER.addPersonToDB(person);
            }
        }
    }

    private static String getRandomText() {
        return getRandomText(10);
    }

    private static String getRandomText(int count) {
        StringBuilder sb = new StringBuilder();
        Random r = new Random();
        char c;
        for(int i = 0; i < count; i++) {
            c = ALPHABET_EN.charAt(r.nextInt(26));
            sb.append(c);
        }
        return sb.toString();
    }

    private static int getRandomNumber() {
        Random r = new Random();
        return r.nextInt(1000000,9999999);
    }
}
