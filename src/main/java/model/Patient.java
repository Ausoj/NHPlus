package model;

import datastorage.CaregiverDAO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Patients live in a NURSING home and are treated by nurses.
 */
public class Patient extends Person {
    private long id;
    private String careLevel;
    private String roomNumber;
    private final List<Treatment> allTreatments = new ArrayList<>();

    /**
     * constructs a patient from the given params.
     *
     * @param firstName the first name of the patient.
     * @param surname  the surname of the patient.
     * @param dateOfBirth the date of birth of the patient.
     * @param careLevel the care level of the patient.
     * @param roomNumber the room number of the patient.
     */
    public Patient(String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomNumber) {
        super(firstName, surname, dateOfBirth);
        this.careLevel = careLevel;
        this.roomNumber = roomNumber;
        throwExceptionWhenRequiredFieldIsEmpty();
    }

    /**
     * constructs a patient from the given params.
     *
     * @param id the id of the patient.
     * @param firstName the first name of the patient.
     * @param surname the surname of the patient.
     * @param dateOfBirth the date of birth of the patient.
     * @param careLevel the care level of the patient.
     * @param roomNumber the room number of the patient.
     */
    public Patient(long id, String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomNumber) {
        super(firstName, surname, dateOfBirth);
        this.id = id;
        this.careLevel = careLevel;
        this.roomNumber = roomNumber;
        throwExceptionWhenRequiredFieldIsEmpty();
    }

    /**
     * @return patient id
     */
    public long getId() {
        return id;
    }

    /**
     * @return careLevel
     */
    public String getCareLevel() {
        return careLevel;
    }

    /**
     * @param careLevel new care level
     */
    public void setCareLevel(String careLevel) {
        this.careLevel = careLevel;
        throwExceptionWhenRequiredFieldIsEmpty();
    }

    /**
     * @return roomNumber as string
     */
    public String getRoomNumber() {
        return roomNumber;
    }

    /**
     * @param roomNumber new room number
     */
    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
        throwExceptionWhenRequiredFieldIsEmpty();
    }


    /**
     * adds a treatment to the treatment-list, if it does not already contain it.
     *
     * @param m Treatment
     * @return true if the treatment was not already part of the list. otherwise false
     */
    public boolean add(Treatment m) {
        if (!this.allTreatments.contains(m)) {
            this.allTreatments.add(m);
            return true;
        }
        return false;
    }

    /**
     * @throws IllegalArgumentException if the required fields are empty and sets the message accordingly.
     */
    private void throwExceptionWhenRequiredFieldIsEmpty() throws IllegalArgumentException {
        if (CaregiverDAO.excludedIds.contains(getPersonId())) return;

        if (Objects.equals(getDateOfBirth().trim(), LocalDate.ofEpochDay(-69420).toString())) {
            throw new IllegalArgumentException("Das Geburtsdatum darf nicht leer sein!");
        } else if (Objects.equals(getCareLevel().trim(), "")) {
            throw new IllegalArgumentException("Der Pflegegrad darf nicht leer sein!");
        } else if (Objects.equals(getRoomNumber().trim(), "")) {
            throw new IllegalArgumentException("Der Raum darf nicht leer sein!");
        }
        try {
            Integer.parseInt(getCareLevel());
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Der Pflegegrad muss eine Zahl sein!");
        }

    }

    /**
     * @return string-representation of the patient
     */
    public String toString() {
        return "Patient" + "\nMNID: " + this.id +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nBirthday: " + this.getDateOfBirth() +
                "\nCareLevel: " + this.careLevel +
                "\nRoomNumber: " + this.roomNumber +
                "\n";
    }
}