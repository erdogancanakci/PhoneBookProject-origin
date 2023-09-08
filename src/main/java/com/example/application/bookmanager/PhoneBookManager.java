package com.example.application.bookmanager;

import com.example.application.data.Person;
import com.vaadin.flow.component.notification.Notification;
import static com.example.application.storage.PersonDataStorage.*;

public class PhoneBookManager {

    public static synchronized void removePerson(Person item) {
        getIDtoPerson().remove(item.getId());
        getIDtoPerson().remove(item.getId());
        showNotification("The person " +item.getName() +" is removed from phonebook");
    }

    public static synchronized void addPerson(Person item) {
        if(isPhoneNumberUnique(item.getPhoneNumber())) {
            getIDtoPerson().put(item.getId(), item);
            getIDtoPhone().put(item.getId(), item.getPhoneNumber());
            showNotification("The person " +item.getName() +" is added to phonebook");
        } else {
            showNotification("Phone number must be unique. The person is not added to Phonebook");
        }
    }

    public static synchronized void updatePerson(Person item) {
        int oldNumber = getIDtoPhone().get(item.getId());
        if(oldNumber == item.getPhoneNumber() || isPhoneNumberUnique(item.getPhoneNumber())) {
            item.setPhoneNumber(item.getPhoneNumber());
            getIDtoPerson().put(item.getId(), item);
            getIDtoPhone().put(item.getId(), item.getPhoneNumber());
            showNotification("The person's information is updated");
        }
        else {
            item.setPhoneNumber(oldNumber);
            showNotification("Phone number must be unique");
        }
    }

    public static boolean isPhoneNumberUnique(int phoneNumber) {
        return !getIDtoPhone().containsValue(phoneNumber);
    }

    public static boolean isIDUnique(int id) {
        return !getIDtoPhone().containsKey(id);
    }

    private static void showNotification(String message) {
        Notification.show(message, 5000, Notification.Position.MIDDLE);
    }

}
