package com.example.application.person_information;

import com.example.application.book_manager.PersonDataStorage;
import java.util.Random;

public class RandomPersonGenerator {
    private static RandomPersonGenerator instance;
    private static final int personCount = 10;
    private RandomPersonGenerator() {
    }

    public static RandomPersonGenerator getInstance()  {
        if(instance == null) {
            instance = new RandomPersonGenerator();
            createRandomPerson();
        }
        return instance;
    }
    private static final String ALPHABET_CAPITAL = "ABCDEFGHIJKLMNOPQRSTUWXVYZ";
    private static final String ALPHABET = "abcdefghijklmnopqrstuwxvyz";

    private static void createRandomPerson() {
        createRandomPerson(personCount);
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

            PersonDataStorage.getPersonMap().put(person.getId(), person);
            PersonDataStorage.getPersonIDMap().put(person.getId(), person.getPhoneNumber());
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
            c = ALPHABET.charAt(r.nextInt(26));
            sb.append(c);
        }
        return sb.toString();
    }

    private static int getRandomNumber() {
        Random r = new Random();
        return r.nextInt(100000000,999999999);
    }
}
