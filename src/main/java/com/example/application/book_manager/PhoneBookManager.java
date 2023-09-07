package com.example.application.book_manager;

import com.example.application.data.Person;
import com.vaadin.flow.component.notification.Notification;
import static com.example.application.storage.PersonDataStorage.*;

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
        getPersonIDtoPerson().remove(item.getId(), item);
        getPersonIDtoPersonPhone().remove(item.getId());
        showNotification("the person " +item.getName() +" is removed from phonebook");
    }

    public static synchronized void addPerson(Person item) {
        getPersonIDtoPerson().put(item.getId(), item);
        getPersonIDtoPersonPhone().put(item.getId(), item.getPhoneNumber());
        showNotification("the person " +item.getName() +" is added to phonebook");
    }

    private static synchronized void updatePerson(Person item) {
        int oldNumber = getPersonIDtoPersonPhone().get(item.getId());
        if(oldNumber == item.getPhoneNumber() || isPhoneNumberUnique(item.getPhoneNumber())) {
            getPersonIDtoPersonPhone().replace(item.getId(), oldNumber, item.getPhoneNumber());
            item.setPhoneNumber(item.getPhoneNumber());
            showNotification("the person's information is updated");
        }
        else {
            item.setPhoneNumber(oldNumber);
            showNotification("phone number must be unique");
        }
    }

    public static boolean isPhoneNumberUnique(int phoneNumber) {
        return !getPersonIDtoPersonPhone().containsValue(phoneNumber);
    }

    public static boolean isIDUnique(int id) {
        return !getPersonIDtoPersonPhone().containsKey(id);
    }

    private static void showNotification(String message) {
        Notification.show(message, 5000, Notification.Position.MIDDLE);
    }

}
