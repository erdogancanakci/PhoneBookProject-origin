package com.example.application.manager;

import com.example.application.data.Person;
import com.vaadin.flow.component.notification.Notification;

import static com.example.application.repository.PersonDataRepository.*;

public class PersonManager {
    private final DBPersonManager dbPersonManager = new DBPersonManager();
    private boolean addedCheck = false;
    private boolean updatedCheck = false;
    private boolean deletedCheck = false;

    public void setAddedCheck(boolean addedCheck) {
        this.addedCheck = addedCheck;
    }

    public void setUpdatedCheck(boolean updatedCheck) {
        this.updatedCheck = updatedCheck;
    }

    public void setDeletedCheck(boolean deletedCheck) {
        this.deletedCheck = deletedCheck;
    }

    public boolean isPersonAdded() {
        return addedCheck;
    }

    public boolean isPersonUpdated() {
        return updatedCheck;
    }

    public boolean isPersonDeleted() {
        return deletedCheck;
    }

    public synchronized void addPersonToPhonebook(Person item) {
        if(getPhoneNumberSet().add(item.getPhoneNumber())) {
            getIdToPersonMap().put(item.getId(), item);
            getIdToPhoneMap().put(item.getId(), item.getPhoneNumber());

            dbPersonManager.addPersonToDB(item);
            addedCheck = true;
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
            updatedCheck = true;
        }
        else {
            dbPersonManager.updatePersonInDBWithoutPhone(item);
            item.setPhoneNumber(oldNumber);
        }
    }

    public synchronized void removePersonFromPhonebook(Person item) {
        if(getPhoneNumberSet().remove(item.getPhoneNumber())) {
            getIdToPersonMap().remove(item.getId(), item);
            getIdToPhoneMap().remove(item.getId(), item.getPhoneNumber());

            dbPersonManager.removePersonFromDB(item);
            deletedCheck = true;
        }
    }
}
