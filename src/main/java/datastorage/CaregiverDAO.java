package datastorage;

import model.Caregiver;
import model.Treatment;
import utils.DateConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CaregiverDAO extends DAOimp<Caregiver> {

    public static final long LOCKED_ID = -6969;
    public static final long DELETED_ID = -69420;
    public static final List<Long> excludedIds = Arrays.asList(LOCKED_ID, DELETED_ID);

    public CaregiverDAO(Connection conn) {
        super(conn);
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
                "WHERE CAREGIVER.ID NOT IN (" + excludedIdsString + ") AND CAREGIVER.ID NOT IN (SELECT ID FROM CAREGIVER_LOCKED)";
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

    public ResultSet getAllCaregiverIdsWithoutATreatmentSince(long unixTime) throws SQLException {
        Statement st = conn.createStatement();
        return st.executeQuery("SELECT CAREGIVER.ID\n" +
                "FROM CAREGIVER\n" +
                "WHERE LAST_TREATMENT < " + unixTime);
    }

    public long getLastTreatmentTime(long key) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery("SELECT LAST_TREATMENT FROM CAREGIVER WHERE ID = " + key);
        result.next();
        return result.getLong(1);
    }

    public void setLastTreatment(Caregiver caregiver) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate("UPDATE CAREGIVER\n" +
                "SET LAST_TREATMENT = " + DateConverter.unixTimestampNow() + "\n" +
                "WHERE ID = " + caregiver.getId());
    }

    private void addCaregiverToLockedTable(long id) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate("INSERT INTO CAREGIVER_LOCKED (ID) VALUES (" + id + ")");
    }

    private void removeCaregiverFromLockedTable(long caregiverId) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate("DELETE FROM CAREGIVER_LOCKED WHERE ID = " + caregiverId);
    }

    public void lockCaregiver(Caregiver caregiver) throws SQLException {
        TreatmentDAO treatmentDAO = DAOFactory.getDAOFactory().createTreatmentDAO();
        long caregiverId = caregiver.getId();
        List<Treatment> treatments = treatmentDAO.readTreatmentsByCid(caregiverId);
        setCaregiverIdOnTreatmentsToLocked(treatments);
        this.addCaregiverToLockedTable(caregiverId);
    }

    public void unlockCaregiver(Caregiver caregiver) throws SQLException {
        TreatmentDAO treatmentDAO = DAOFactory.getDAOFactory().createTreatmentDAO();
//      Todo: fix being unable to set the previous treatments of this caregiver because there exists no reference
        long caregiverId = caregiver.getId();
        this.removeCaregiverFromLockedTable(caregiverId);

    }


    public void deleteCaregiver(Caregiver caregiver) throws SQLException {
        PersonDAO personDAO = DAOFactory.getDAOFactory().createPersonDAO();
        TreatmentDAO treatmentDAO = DAOFactory.getDAOFactory().createTreatmentDAO();
        long caregiverId = caregiver.getId();
        unlockCaregiver(caregiver);
        List<Treatment> treatments = treatmentDAO.readTreatmentsByCid(caregiverId);
        setCaregiverIdOnTreatmentsToDeleted(treatments);
        this.deleteById(caregiverId);
        personDAO.deleteById(caregiver.getPersonId());
    }

    private void setCaregiverIdOnTreatmentsToDeleted(List<Treatment> treatments) throws SQLException {
        TreatmentDAO treatmentDAO = DAOFactory.getDAOFactory().createTreatmentDAO();
        for (Treatment treatment : treatments) {
            treatment.setCaregiverId(CaregiverDAO.DELETED_ID);
            treatmentDAO.updateWithoutLastChange(treatment);
        }
    }

    private void setCaregiverIdOnTreatmentsToLocked(List<Treatment> treatments) throws SQLException {
        TreatmentDAO treatmentDAO = DAOFactory.getDAOFactory().createTreatmentDAO();
        for (Treatment treatment : treatments) {
            treatment.setCaregiverId(CaregiverDAO.LOCKED_ID);
            treatmentDAO.updateWithoutLastChange(treatment);
        }
    }
}
