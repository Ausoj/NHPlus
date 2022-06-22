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
    private String roomnumber;
    private List<Treatment> allTreatments = new ArrayList<Treatment>();

    /**
     * constructs a patient from the given params.
     *
     * @param firstName
     * @param surname
     * @param dateOfBirth
     * @param careLevel
     * @param roomnumber
     */
    public Patient(String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomnumber) {
        super(firstName, surname, dateOfBirth);
        this.careLevel = careLevel;
        this.roomnumber = roomnumber;
        throwExceptionWhenRequiredFieldIsEmpty();
    }

    /**
     * constructs a patient from the given params.
     *
     * @param id
     * @param firstName
     * @param surname
     * @param dateOfBirth
     * @param careLevel
     * @param roomnumber
     */
    public Patient(long id, String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomnumber) {
        super(firstName, surname, dateOfBirth);
        this.id = id;
        this.careLevel = careLevel;
        this.roomnumber = roomnumber;
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
    public String getRoomnumber() {
        return roomnumber;
    }

    /**
     * @param roomnumber
     */
    public void setRoomnumber(String roomnumber) {
        this.roomnumber = roomnumber;
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

    private void throwExceptionWhenRequiredFieldIsEmpty() throws IllegalArgumentException {
        if (CaregiverDAO.excludedIds.contains(getPersonId())) return;

        if (Objects.equals(getDateOfBirth().trim(), LocalDate.ofEpochDay(-69420).toString())) {
            throw new IllegalArgumentException("Das Geburtsdatum darf nicht leer sein!");
        } else if (Objects.equals(getCareLevel().trim(), "")) {
            throw new IllegalArgumentException("Der Pflegegrad darf nicht leer sein!");
        } else if (Objects.equals(getRoomnumber().trim(), "")) {
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
                "\nCarelevel: " + this.careLevel +
                "\nRoomnumber: " + this.roomnumber +
                "\n";
    }
}