package datastorage;

import model.Caregiver;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CaregiverDAO extends DAOimp<Caregiver> {

    public static final long LOCKED_ID = -6969;
    public static final long DELETED_ID = -69420;
    private final List<Long> excludedIds = new ArrayList<>();

    public CaregiverDAO(Connection conn) {
        super(conn);
        excludedIds.add(LOCKED_ID);
        excludedIds.add(DELETED_ID);
    }

    @Override
    protected String getCreateStatementString(Caregiver caregiver) {
        return String.format("INSERT INTO CAREGIVER (PERSON_ID, PHONE_NUMBER) VALUES (%d, '%s')",
                caregiver.getPersonId(), caregiver.getPhoneNumber());
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
        StringBuilder excludedIdsString = new StringBuilder();
        for (long id :
                excludedIds) {
            excludedIdsString.append(id);
            if (id != excludedIds.get(excludedIds.size() - 1)) excludedIdsString.append(",");
        }
        return "SELECT CAREGIVER.ID, PERSON.FIRSTNAME, PERSON.SURNAME, CAREGIVER.PHONE_NUMBER FROM CAREGIVER\n" +
                "JOIN PERSON on PERSON.ID = CAREGIVER.PERSON_ID\n" +
                "WHERE CAREGIVER.ID NOT IN (" + excludedIdsString + ")";
    }

    @Override
    protected ArrayList<Caregiver> getListFromResultSet(ResultSet set) throws SQLException {
        ArrayList<Caregiver> list = new ArrayList<>();
        Caregiver c = null;
        while (set.next()) {
            c = getInstanceFromResultSet(set);
            list.add(c);
        }
        return list;
    }

    @Override
    protected String getUpdateStatementString(Caregiver caregiver) {
        return String.format("UPDATE CAREGIVER SET PERSON_ID = %d, PHONE_NUMBER = '%s' WHERE ID = %d", caregiver.getPersonId(), caregiver.getPhoneNumber(), caregiver.getId());
    }

    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("DELETE FROM CAREGIVER WHERE ID = %d", key);
    }
}
