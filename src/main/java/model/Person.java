package model;

import datastorage.DAOFactory;
import datastorage.PersonDAO;
import utils.DateConverter;

import java.sql.SQLException;
import java.time.LocalDate;

public abstract class Person {
    private long id;
    private String firstName;
    private String surname;

    private LocalDate dateOfBirth;

    public Person(String firstName, String surname, LocalDate dateOfBirth) {
        PersonDAO dao = DAOFactory.getDAOFactory().createPersonDAO();

        this.firstName = firstName;
        this.surname = surname;
        this.dateOfBirth = dateOfBirth;

        try {
            if (dao.getIdByInstance(this) == -1) {
                dao.create(this);
            }
            this.id = dao.getIdByInstance(this);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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
    }
}
