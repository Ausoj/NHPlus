package model;

import utils.DateConverter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Patients live in a NURSING home and are treated by nurses.
 */
public class Patient extends Person {
    private long pid;
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
    }

    /**
     * constructs a patient from the given params.
     *
     * @param pid
     * @param firstName
     * @param surname
     * @param dateOfBirth
     * @param careLevel
     * @param roomnumber
     */
    public Patient(long pid, String firstName, String surname, LocalDate dateOfBirth, String careLevel, String roomnumber) {
        super(firstName, surname, dateOfBirth);
        this.pid = pid;
        this.careLevel = careLevel;
        this.roomnumber = roomnumber;
    }

    /**
     * @return patient id
     */
    public long getPid() {
        return pid;
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
//        TODO: currently throws error if careLevel is not a number since the db currently requires an integer
        this.careLevel = careLevel;
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
     * @return string-representation of the patient
     */
    public String toString() {
        return "Patient" + "\nMNID: " + this.pid +
                "\nFirstname: " + this.getFirstName() +
                "\nSurname: " + this.getSurname() +
                "\nBirthday: " + this.getDateOfBirth() +
                "\nCarelevel: " + this.careLevel +
                "\nRoomnumber: " + this.roomnumber +
                "\n";
    }
}