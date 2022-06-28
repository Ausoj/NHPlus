package model;

import datastorage.CaregiverDAO;
import datastorage.DAOFactory;
import datastorage.PersonDAO;
import utils.DateConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

/**
 * This class represents a person.
 */
public abstract class Person {
    private long personId;
    private String firstName;
    private String surname;
    private LocalDate dateOfBirth;


    /**
     * @param firstName the first name of the person
     * @param surname the surname of the person
     */
    public Person(String firstName, String surname) {
        PersonDAO dao = DAOFactory.getDAOFactory().createPersonDAO();

        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = LocalDate.ofEpochDay(-69420);

        try {
            if (dao.getIdByInstance(this) == -1) {
                dao.create(this);
            }
            this.personId = dao.getIdByInstance(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throwExceptionWhenRequiredFieldIsEmpty();
    }

    /**
     * @param firstName the first name of the person
     * @param surname the surname of the person
     * @param dateOfBirth the date of birth of the person
     */
    public Person(String firstName, String surname, LocalDate dateOfBirth) {
        PersonDAO dao = DAOFactory.getDAOFactory().createPersonDAO();

        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;

        try {
            if (dao.getIdByInstance(this) == -1) {
                dao.create(this);
            }
            this.personId = dao.getIdByInstance(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throwExceptionWhenRequiredFieldIsEmpty();
    }

    /**
     * @return the personId
     */
    public long getPersonId() {
        return personId;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
        throwExceptionWhenRequiredFieldIsEmpty();
    }

    /**
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * @param surname the surname to set
     */
    public void setSurname(String surname) {
        this.surname = surname;
        throwExceptionWhenRequiredFieldIsEmpty();
    }

    /**
     * @return date of birth as a string
     */
    public String getDateOfBirth() {
        return dateOfBirth.toString();
    }

    /**
     * convert given param to a localDate and store as new <code>birthOfDate</code>
     *
     * @param dateOfBirth as string in the following format: YYYY-MM-DD
     */
    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = DateConverter.convertStringToLocalDate(dateOfBirth);
        throwExceptionWhenRequiredFieldIsEmpty();
    }

    /**
     * @throws IllegalArgumentException if required field is empty and sets the message accordingly
     */
    private void throwExceptionWhenRequiredFieldIsEmpty() throws IllegalArgumentException {
        if (CaregiverDAO.excludedIds.contains(getPersonId())) return;

        if (Objects.equals(getSurname().trim(), "")) {
            throw new IllegalArgumentException("Der Nachname darf nicht leer sein!");
        } else if (Objects.equals(getFirstName().trim(), "")) {
            throw new IllegalArgumentException("Der Vorname darf nicht leer sein!");
        }
    }

    /**
     * @return the abbreviated name of the person
     */
    public String getAbbreviatedName() {
        return hasFirstname() ? String.format("%s. %s", this.firstName.charAt(0), this.getSurname()) : this.getSurname();
    }

    /**
     * @return true if the person has a firstname
     */
    private boolean hasFirstname() {
        return this.firstName.length() != 0;
    }
}
