package model;

import datastorage.CaregiverDAO;

import java.util.Objects;

public class Caregiver extends Person {

    private long id;
    private String phoneNumber;

    public Caregiver(String firstName, String surname, String phoneNumber) {
        super(firstName, surname);
        this.phoneNumber = phoneNumber;
        throwExceptionWhenRequiredFieldIsEmpty();

    }

    public Caregiver(long id, String firstName, String surname, String phoneNumber) {
        super(firstName, surname);
        this.id = id;
        this.phoneNumber = phoneNumber;
        throwExceptionWhenRequiredFieldIsEmpty();
    }

    private void throwExceptionWhenRequiredFieldIsEmpty() {
        if (CaregiverDAO.excludedIds.contains(getId())) return;

        if (Objects.equals(getPhoneNumber().trim(), "")) {
            throw new IllegalArgumentException("Die Telefonnummer darf nicht leer sein.");
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        throwExceptionWhenRequiredFieldIsEmpty();
    }

    public String toString() {
        return "Caregiver" + "\nID: " + this.id +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nPhoneNumber: " + this.getPhoneNumber() +
                "\n";
    }
}
