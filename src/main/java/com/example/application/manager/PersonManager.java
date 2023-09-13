package com.example.application.manager;

import com.example.application.data.Person;
import com.vaadin.flow.component.notification.Notification;

import static com.example.application.repository.PersonDataRepository.*;

public class PersonManager {
    private final DBPersonManager dbPersonManager = new DBPersonManager();

    public synchronized void addPersonToPhonebook(Person item) {
        if(getPhoneNumberSet().add(item.getPhoneNumber())) {
            getIdToPersonMap().put(item.getId(), item);
            getIdToPhoneMap().put(item.getId(), item.getPhoneNumber());

            dbPersonManager.addPersonToDB(item);
            showNotification("The person " +item.getName() +" is added to phonebook");
        } else {
            showNotification("The phone number must be unique. The person is not added to Phonebook");
        }
    }

    public synchronized void updatePersonInPhonebook(Person item) {
        int oldNumber = getIdToPhoneMap().get(item.getId());
        int currentNumber = item.getPhoneNumber();
        if(getPhoneNumberSet().add(currentNumber) || (oldNumber == currentNumber)) {
            if(oldNumber != currentNumber) {
                getPhoneNumberSet().remove(oldNumber);
                getIdToPhoneMap().replace(item.getId(), oldNumber, currentNumber);
            }

            dbPersonManager.updatePersonInDB(item);
            showNotification("The person's information is updated");
        }
        else {
            dbPersonManager.updatePersonInDBWithoutPhone(item);
            item.setPhoneNumber(oldNumber);
            showNotification("The phone number must be unique");
        }
    }

    public synchronized void removePersonFromPhonebook(Person item) {
        if(getPhoneNumberSet().remove(item.getPhoneNumber())) {
            getIdToPersonMap().remove(item.getId(), item);
            getIdToPhoneMap().remove(item.getId(), item.getPhoneNumber());

            dbPersonManager.removePersonFromDB(item);
            showNotification("The person " +item.getName() +" is removed from phonebook");
        }
        else {
            showNotification("The person alredy deleted");
        }
    }

    private void showNotification(String message) {
        Notification.show(message, 5000, Notification.Position.MIDDLE);
    }

}
