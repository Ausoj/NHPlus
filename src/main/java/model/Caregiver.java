package model;

import datastorage.CaregiverDAO;

import java.util.Objects;

public class Caregiver extends Person {

    private long id;
    private String phoneNumber;

    public Caregiver(String firstName, String surname, String phoneNumber) {
        super(firstName, surname);
        this.phoneNumber = phoneNumber;
        throwExceptionWhenRequieredFieldIsEmpty();

    }

    public Caregiver(long id, String firstName, String surname, String phoneNumber) {
        super(firstName, surname);
        this.id = id;
        this.phoneNumber = phoneNumber;
        throwExceptionWhenRequieredFieldIsEmpty();
    }

    private void throwExceptionWhenRequieredFieldIsEmpty(){
        if (CaregiverDAO.excludedIds.contains(getId())) return;
        if (Objects.equals(getSurname(), "")){
            throw new IllegalArgumentException("Bitte füge einen Nachnamen hinzu!");
        } else if (Objects.equals(getFirstName(), "")) {
            throw new IllegalArgumentException("Bitte füge einen Vornamen hinzu!");
        } else if (Objects.equals(getPhoneNumber(), "")) {
            throw new IllegalArgumentException("Bitte füge eine Telefonnummer hinzu!");
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
    }

    public String toString() {
        return "Caregiver" + "\nID: " + this.id +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nPhoneNumber: " + this.getPhoneNumber() +
                "\n";
    }
}
