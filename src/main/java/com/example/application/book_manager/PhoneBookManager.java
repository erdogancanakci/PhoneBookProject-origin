package com.example.application.book_manager;

import com.example.application.person_information.Person;
import com.vaadin.flow.component.notification.Notification;


import static com.example.application.book_manager.PersonDataStorage.*;

public class PhoneBookManager {

    public static synchronized void saveOrUpdatePerson(Person item) {

        if(isIDUnique(item.getId()) ) {
            if(isPhoneNumberUnique(item.getPhoneNumber())) {
                addPerson(item);
            }
            else {
                showNotification("phone number must be unique");
            }
        }
        else {
            updatePerson(item);
        }
    }

    public static synchronized void removePerson(Person item) {
        getPersonMap().remove(item.getId(), item);
        getPersonIDMap().remove(item.getId());
        showNotification("the person " +item.getName() +" is removed from phonebook");
    }

    public static synchronized void addPerson(Person item) {
        getPersonMap().put(item.getId(), item);
        getPersonIDMap().put(item.getId(), item.getPhoneNumber());
        showNotification("the person " +item.getName() +" is added to phonebook");
    }

    private static synchronized void updatePerson(Person item) {
        int oldNumber = getPersonIDMap().get(item.getId());
        if(isPhoneNumberUnique(item.getPhoneNumber())) {
            getPersonIDMap().replace(item.getId(), oldNumber, item.getPhoneNumber());
            item.setPhoneNumber(item.getPhoneNumber());
            showNotification("the person's number is updated");
        }
        else {
            item.setPhoneNumber(oldNumber);
            showNotification("Person information is updated");
        }
    }

    public static boolean isPhoneNumberUnique(int phoneNumber) {
        return !getPersonIDMap().containsValue(phoneNumber);
    }

    public static boolean isIDUnique(int id) {
        return !getPersonIDMap().containsKey(id);
    }

    private static void showNotification(String message) {
        Notification.show(message, 5000, Notification.Position.MIDDLE);
    }
}
