package com.example.application.person_information;

import com.example.application.book_manager.PersonDataStorage;
import java.util.Random;

public class RandomPersonGenerator {
    private static final int personCount = 10;
    private static final String ALPHABET_CAPITAL_EN = "ABCDEFGHIJKLMNOPQRSTUWXVYZ";
    private static final String ALPHABET_EN = "abcdefghijklmnopqrstuwxvyz";
    private static RandomPersonGenerator instance;
    private RandomPersonGenerator() {
    }

    public static RandomPersonGenerator getInstance()  {
        if(instance == null) {
            instance = new RandomPersonGenerator();
            createRandomPerson();
        }
        return instance;
    }

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
            c = ALPHABET_EN.charAt(r.nextInt(26));
            sb.append(c);
        }
        return sb.toString();
    }

    private static int getRandomNumber() {
        Random r = new Random();
        return r.nextInt(100000,999999);
    }
}
