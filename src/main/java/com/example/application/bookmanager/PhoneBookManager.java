package com.example.application.bookmanager;

import com.example.application.data.Person;
import com.vaadin.flow.component.notification.Notification;
import static com.example.application.storage.PersonDataStorage.*;

public class PhoneBookManager {

    public static synchronized void removePerson(Person item) {
        getIDtoPersonMap().remove(item.getId());
        getIDtoPhoneMap().remove(item.getId());
        showNotification("The person " +item.getName() +" is removed from phonebook");
    }

    public static synchronized void addPerson(Person item) {
        if(isPhoneNumberUnique(item.getPhoneNumber())) {
            getIDtoPersonMap().put(item.getId(), item);
            getIDtoPhoneMap().put(item.getId(), item.getPhoneNumber());
            showNotification("The person " +item.getName() +" is added to phonebook");
        } else {
            showNotification("Phone number must be unique. The person is not added to Phonebook");
        }
    }

    public static synchronized void updatePerson(Person item) {
        int oldNumber = getIDtoPhoneMap().get(item.getId());
        if(oldNumber == item.getPhoneNumber() || isPhoneNumberUnique(item.getPhoneNumber())) {
            item.setPhoneNumber(item.getPhoneNumber());
            getIDtoPersonMap().put(item.getId(), item);
            getIDtoPhoneMap().put(item.getId(), item.getPhoneNumber());
            showNotification("The person's information is updated");
        }
        else {
            item.setPhoneNumber(oldNumber);
            showNotification("Phone number must be unique");
        }
    }

    public static boolean isPhoneNumberUnique(int phoneNumber) {
        return !getIDtoPhoneMap().containsValue(phoneNumber);
    }

    public static boolean isIDUnique(int id) {
        return !getIDtoPhoneMap().containsKey(id);
    }

    private static void showNotification(String message) {
        Notification.show(message, 5000, Notification.Position.MIDDLE);
    }

}
