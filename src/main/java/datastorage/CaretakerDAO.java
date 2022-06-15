package datastorage;

import model.Caretaker;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CaretakerDAO extends DAOimp<Caretaker> {
    public CaretakerDAO(Connection conn) {
        super(conn);
    }

    @Override
    protected String getCreateStatementString(Caretaker caretaker) {
        return String.format("INSERT INTO CARETAKER (PERSON_ID, PHONE_NUMBER) VALUES (%d, '%s')",
                caretaker.getId(), caretaker.getPhoneNumber());
    }

    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT CARETAKER.ID, PERSON.FIRSTNAME, PERSON.SURNAME, CARETAKER.PHONE_NUMBER FROM CARETAKER\n" +
                "JOIN PERSON on PERSON.ID = CARETAKER.PERSON_ID\n" +
                "WHERE ID = %d", key);
    }

    @Override
    protected Caretaker getInstanceFromResultSet(ResultSet set) throws SQLException {
         Caretaker c = null;
         c = new Caretaker(set.getLong(1), set.getString(2), set.getString(3), set.getString(4));
         return c;
    }

    @Override
    protected String getReadAllStatementString() {
        return "SELECT CARETAKER.ID, PERSON.FIRSTNAME, PERSON.SURNAME, CARETAKER.PHONE_NUMBER FROM CARETAKER\n" +
                "JOIN PERSON on PERSON.ID = CARETAKER.PERSON_ID";
    }

    @Override
    protected ArrayList<Caretaker> getListFromResultSet(ResultSet set) throws SQLException {
        ArrayList<Caretaker> list = new ArrayList<>();
        Caretaker c = null;
        while (set.next()){
            c = getInstanceFromResultSet(set);
            list.add(c);
        }
        return list;
    }

    @Override
    protected String getUpdateStatementString(Caretaker caretaker) {
        return String.format("UPDATE CARETAKER SET PERSON_ID = %d, PHONE_NUMBER = '%s' WHERE ID = %d", caretaker.getId(), caretaker.getPhoneNumber(), caretaker.getCid());
    }

    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("DELETE FROM CARETAKER WHERE ID = %d", key);
    }
}
