package datastorage;

import model.Caregiver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CaregiverDAO extends DAOimp<Caregiver> {
    public CaregiverDAO(Connection conn) {
        super(conn);
    }

    @Override
    protected String getCreateStatementString(Caregiver caregiver) {
        return String.format("INSERT INTO CAREGIVER (PERSON_ID, PHONE_NUMBER) VALUES (%d, '%s')",
                caregiver.getId(), caregiver.getPhoneNumber());
    }

    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT CAREGIVER.ID, PERSON.FIRSTNAME, PERSON.SURNAME, CAREGIVER.PHONE_NUMBER FROM CAREGIVER\n" +
                "JOIN PERSON on PERSON.ID = CAREGIVER.PERSON_ID\n" +
                "WHERE ID = %d", key);
    }

    @Override
    protected Caregiver getInstanceFromResultSet(ResultSet set) throws SQLException {
         Caregiver c = null;
         c = new Caregiver(set.getLong(1), set.getString(2), set.getString(3), set.getString(4));
         return c;
    }

    @Override
    protected String getReadAllStatementString() {
        return "SELECT CAREGIVER.ID, PERSON.FIRSTNAME, PERSON.SURNAME, CAREGIVER.PHONE_NUMBER FROM CAREGIVER\n" +
                "JOIN PERSON on PERSON.ID = CAREGIVER.PERSON_ID";
    }

    @Override
    protected ArrayList<Caregiver> getListFromResultSet(ResultSet set) throws SQLException {
        ArrayList<Caregiver> list = new ArrayList<>();
        Caregiver c = null;
        while (set.next()){
            c = getInstanceFromResultSet(set);
            list.add(c);
        }
        return list;
    }

    @Override
    protected String getUpdateStatementString(Caregiver caregiver) {
        return String.format("UPDATE CAREGIVER SET PERSON_ID = %d, PHONE_NUMBER = '%s' WHERE ID = %d", caregiver.getId(), caregiver.getPhoneNumber(), caregiver.getCid());
    }

    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("DELETE FROM CAREGIVER WHERE ID = %d", key);
    }
}
