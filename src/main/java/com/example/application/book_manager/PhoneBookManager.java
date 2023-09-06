package com.example.application.book_manager;

import com.example.application.person_information.Person;
import com.vaadin.flow.component.notification.Notification;


import static com.example.application.book_manager.PersonDataStorage.*;

public class PhoneBookManager {


    public static synchronized void savePerson(Person item) {

        if(isIDUnique(item.getId()) ) {
            if(isPhoneNumberUnique(item.getPhoneNumber())) {
                addPerson(item);
            }
            else {
                Notification.show("phone number must be unique", 5000, Notification.Position.MIDDLE);
            }
        }
        else {
            int oldNumber = getPersonIDMap().get(item.getId());
            if(isPhoneNumberUnique(item.getPhoneNumber())) {
                getPersonIDMap().replace(item.getId(), oldNumber, item.getPhoneNumber());
                item.setPhoneNumber(item.getPhoneNumber());
                Notification.show("the person's number is updated", 5000, Notification.Position.MIDDLE);
            }
            else {
                item.setPhoneNumber(oldNumber);
                Notification.show("phone number must be unique", 5000, Notification.Position.MIDDLE);
            }
        }
    }

    public static synchronized void deletePerson(Person item) {
        getPersonMap().remove(item.getId(), item);
        getPersonIDMap().remove(item.getId());
        Notification.show("the person " +item.getName() +" is deleted from phonebook", 5000, Notification.Position.MIDDLE);
    }

    public static synchronized void addPerson(Person item) {
        getPersonMap().put(item.getId(), item);
        getPersonIDMap().put(item.getId(), item.getPhoneNumber());
        Notification.show("the person " +item.getName() +" is added to phonebook", 5000, Notification.Position.MIDDLE);
    }


    public static boolean isPhoneNumberUnique(int phoneNumber) {
        return !getPersonIDMap().containsValue(phoneNumber);
    }

    public static boolean isIDUnique(int id) {
        return !getPersonIDMap().containsKey(id);
    }
}
