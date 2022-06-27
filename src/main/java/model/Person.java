package model;

import datastorage.CaregiverDAO;
import datastorage.DAOFactory;
import datastorage.PersonDAO;
import utils.DateConverter;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Objects;

public abstract class Person {
    private long personId;
    private String firstName;
    private String surname;
    private LocalDate dateOfBirth;


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

    public long getPersonId() {
        return personId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        throwExceptionWhenRequiredFieldIsEmpty();
    }

    public String getSurname() {
        return surname;
    }

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

    private void throwExceptionWhenRequiredFieldIsEmpty() throws IllegalArgumentException {
        if (CaregiverDAO.excludedIds.contains(getPersonId())) return;

        if (Objects.equals(getSurname().trim(), "")) {
            throw new IllegalArgumentException("Der Nachname darf nicht leer sein!");
        } else if (Objects.equals(getFirstName().trim(), "")) {
            throw new IllegalArgumentException("Der Vorname darf nicht leer sein!");
        }
    }

    public String getAbbreviatedName() {
        return hasFirstname() ? String.format("%s. %s", this.firstName.charAt(0), this.getSurname()) : this.getSurname();
    }

    private boolean hasFirstname() {
        return this.firstName.length() != 0;
    }
}
