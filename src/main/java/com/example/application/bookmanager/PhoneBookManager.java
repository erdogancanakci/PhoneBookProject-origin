package com.example.application.bookmanager;

import com.example.application.data.Person;
import com.vaadin.flow.component.notification.Notification;
import static com.example.application.storage.PersonDataStorage.*;

public class PhoneBookManager {

    public static synchronized void addPerson(Person item) {
        if(getPhoneNumberSet().add(item.getPhoneNumber())) {
            getIdToPersonMap().put(item.getId(), item);
            getIdToPersonPhoneMap().put(item.getId(), item.getPhoneNumber());
            showNotification("The person " +item.getName() +" is added to phonebook");
        } else {
            showNotification("The phone number must be unique. The person is not added to Phonebook");
        }
    }

    public static synchronized void updatePerson(Person item) {
        int oldNumber = getIdToPersonPhoneMap().get(item.getId());
        int currentNumber = item.getPhoneNumber();
        if(getPhoneNumberSet().add(currentNumber) || (oldNumber == currentNumber)) {
            if(oldNumber != currentNumber) {
                getPhoneNumberSet().remove(oldNumber);
                getIdToPersonPhoneMap().replace(item.getId(), oldNumber, currentNumber);
            }
            showNotification("The person's information is updated");
        }
        else {
            item.setPhoneNumber(oldNumber);
            showNotification("The phone number must be unique");
        }
    }

    public static synchronized void removePerson(Person item) {
        if(getPhoneNumberSet().remove(item.getPhoneNumber())) {
            getIdToPersonMap().remove(item.getId(), item);
            getIdToPersonPhoneMap().remove(item.getId(), item.getPhoneNumber());
            showNotification("The person " +item.getName() +" is removed from phonebook");
        }
        else {
            showNotification("The person alredy deleted");
        }
    }

    private static void showNotification(String message) {
        Notification.show(message, 5000, Notification.Position.MIDDLE);
    }

}
