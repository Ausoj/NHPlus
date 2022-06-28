package datastorage;

import model.Person;
import utils.DateConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * This class is used to access the database and retrieve data from it regarding the persons.
 */
public class PersonDAO extends DAOimp<Person> {
    /**
     * @param conn The connection to the database.
     */
    public PersonDAO(Connection conn) {
        super(conn);
    }

    /**
     * @param person the person to be added to the database.
     * @return the query string.
     */
    @Override
    protected String getCreateStatementString(Person person) {
        return String.format("INSERT INTO PERSON (FIRSTNAME, SURNAME, DATE_OF_BIRTH) VALUES ('%s', '%s', %d);",
                person.getFirstName(), person.getSurname(), DateConverter.convertStringToUnixTimestamp(person.getDateOfBirth()));
    }

    /**
     * @param key the id of the person to be fetched.
     * @return the query string.
     */
    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT ID, FIRSTNAME, SURNAME, DATE_OF_BIRTH FROM PERSON WHERE ID = %d", key);
    }

    @Override
    protected Person getInstanceFromResultSet(ResultSet result) {
        return null;
    }

    /**
     * @return the query string.
     */
    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM PERSON";
    }

    @Override
    protected ArrayList<Person> getListFromResultSet(ResultSet set) {
        return null;
    }

    /**
     * @param person the person to be updated in the database.
     * @return the query string.
     */
    @Override
    protected String getUpdateStatementString(Person person) {
        return String.format("UPDATE PERSON SET FIRSTNAME = '%s', SURNAME = '%s', DATE_OF_BIRTH = %d WHERE ID = %d",
                person.getFirstName(), person.getSurname(), DateConverter.convertStringToUnixTimestamp(person.getDateOfBirth()), person.getPersonId());
    }

    /**
     * @param key the id of the person to be deleted from the database.
     * @return the query string.
     */
    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("DELETE FROM PERSON WHERE ID = %d", key);
    }

    /**
     * @param instance the person to be added to the database.
     * @return the query string.
     */
    public long getIdByInstance(Person instance) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(String.format("SELECT ID FROM PERSON WHERE FIRSTNAME = '%s' AND SURNAME = '%s' AND DATE_OF_BIRTH = %d",
                instance.getFirstName(), instance.getSurname(), DateConverter.convertStringToUnixTimestamp(instance.getDateOfBirth())));
        if (result.next()) {
            return result.getLong(1);
        }
        return -1;
    }
}
