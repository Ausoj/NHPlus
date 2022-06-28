package model;

import datastorage.CaregiverDAO;

import java.util.Objects;

/**
 *  Class that represents a caregiver.
 */
public class Caregiver extends Person {

    private long id;
    private String phoneNumber;

    /**
     * @param firstName first name of the caregiver
     * @param surname surname of the caregiver
     * @param phoneNumber phone number of the caregiver
     */
    public Caregiver(String firstName, String surname, String phoneNumber) {
        super(firstName, surname);
        this.phoneNumber = phoneNumber;
        throwExceptionWhenRequiredFieldIsEmpty();

    }

    /**
     * @param id id of the caregiver
     * @param firstName first name of the caregiver
     * @param surname surname of the caregiver
     * @param phoneNumber phone number of the caregiver
     */
    public Caregiver(long id, String firstName, String surname, String phoneNumber) {
        super(firstName, surname);
        this.id = id;
        this.phoneNumber = phoneNumber;
        throwExceptionWhenRequiredFieldIsEmpty();
    }

    /**
     * @throws IllegalArgumentException if any of the required fields is empty
     */
    private void throwExceptionWhenRequiredFieldIsEmpty() throws IllegalArgumentException {
        if (CaregiverDAO.excludedIds.contains(getId())) return;

        if (Objects.equals(getPhoneNumber().trim(), "")) {
            throw new IllegalArgumentException("Die Telefonnummer darf nicht leer sein.");
        }

    }

    /**
     * @return id of the caregiver
     */
    public long getId() {
        return id;
    }

    /**
     * @return phone number of the caregiver
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @param phoneNumber phone number of the caregiver
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        throwExceptionWhenRequiredFieldIsEmpty();
    }

    /**
     * @return string representation of the caregiver
     */
    public String toString() {
        return "Caregiver" + "\nID: " + this.id +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nPhoneNumber: " + this.getPhoneNumber() +
                "\n";
    }
}
