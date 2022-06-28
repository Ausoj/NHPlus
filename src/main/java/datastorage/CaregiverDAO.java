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

/**
 * This class is used to access the database and to store the data related to Caregivers.
 */
public class CaregiverDAO extends DAOimp<Caregiver> {

    /**
     * ID for locked Caregivers/Persons
     */
    public static final long LOCKED_ID = -6969;


    /**
     * ID for DELETED Caregivers/Persons
     */
    public static final long DELETED_ID = -69420;
    /**
     * {@link List} of ids that should be excluded.
     */
    public static final List<Long> excludedIds = Arrays.asList(LOCKED_ID, DELETED_ID);

    /**
     * @param conn The connection to the database.
     */
    public CaregiverDAO(Connection conn) {
        super(conn);
    }

    /**
     * @param caregiver to be inserted into the database
     * @return SQL-Statement to insert the caregiver into the database
     */
    @Override
    protected String getCreateStatementString(Caregiver caregiver) {
        return String.format("INSERT INTO CAREGIVER (PERSON_ID, PHONE_NUMBER) VALUES (%d, '%s')",
                caregiver.getPersonId(), caregiver.getPhoneNumber());
    }

    /**
     * @param key to be used to find the caregiver (id) in the database
     * @return SQL-Statement to find the caregiver in the database
     */
    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT CAREGIVER.ID, PERSON.FIRSTNAME, PERSON.SURNAME, CAREGIVER.PHONE_NUMBER FROM CAREGIVER\n" +
                "JOIN PERSON on PERSON.ID = CAREGIVER.PERSON_ID\n" +
                "WHERE ID = %d", key);
    }

    /**
     * @param set {@link ResultSet} of a caregiver to be instantiated
     * @return caregiver instantiated from the {@link ResultSet}
     */
    @Override
    protected Caregiver getInstanceFromResultSet(ResultSet set) throws SQLException {
        Caregiver c;
        c = new Caregiver(set.getLong(1), set.getString(2), set.getString(3), set.getString(4));
        return c;
    }

    /**
     * @return SQL-Statement to fetch all Caregivers in the database
     */
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

    /**
     * @param set {@link ResultSet} of caregiver data
     * @return list of caregivers instantiated from the {@link ResultSet}
     */
    @Override
    protected ArrayList<Caregiver> getListFromResultSet(ResultSet set) throws SQLException {
        ArrayList<Caregiver> list = new ArrayList<>();
        Caregiver c;
        while (set.next()) {
            c = getInstanceFromResultSet(set);
            list.add(c);
        }
        return list;
    }

    /**
     * @param caregiver to be updated in the database
     * @return SQL-Statement to update the caregiver in the database
     */
    @Override
    protected String getUpdateStatementString(Caregiver caregiver) {
        return String.format("UPDATE CAREGIVER SET PERSON_ID = %d, PHONE_NUMBER = '%s' WHERE ID = %d", caregiver.getPersonId(), caregiver.getPhoneNumber(), caregiver.getId());
    }

    /**
     * @param key used to delete the caregiver from the database by id
     * @return SQL-Statement to delete the caregiver from the database
     */
    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("DELETE FROM CAREGIVER WHERE ID = %d", key);
    }

    /**
     * @param unixTime used to find the caregivers that have not had a treatment since the given time
     * @return SQL-Statement to find the caregivers that have not had a treatment since the given time
     *
     * @throws SQLException if the query fails
     */
    public ResultSet getAllCaregiverIdsWithoutATreatmentSince(long unixTime) throws SQLException {
        Statement st = conn.createStatement();
        return st.executeQuery("SELECT CAREGIVER.ID\n" +
                "FROM CAREGIVER\n" +
                "WHERE LAST_TREATMENT < " + unixTime);
    }

    /**
     * @param key used to find the caregiver in the database
     * @return SQL-Statement to fetch the caregivers last treatment time in the database
     *
     * @throws SQLException if the query fails
     */
    public long getLastTreatmentTime(long key) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery("SELECT LAST_TREATMENT FROM CAREGIVER WHERE ID = " + key);
        result.next();
        return result.getLong(1);
    }

    /**
     * @param caregiver to update the last treatment time
     *
     * @throws SQLException if the update fails
     */
    public void setLastTreatment(Caregiver caregiver) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate("UPDATE CAREGIVER\n" +
                "SET LAST_TREATMENT = " + DateConverter.unixTimestampNow() + "\n" +
                "WHERE ID = " + caregiver.getId());
    }

    /**
     * @param id of the caregiver to be locked
     */
    private void addCaregiverToLockedTable(long id) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate("INSERT INTO CAREGIVER_LOCKED (ID) VALUES (" + id + ")");
    }

    /**
     * @param caregiverId of the caregiver to be removed from the locked table
     */
    private void removeCaregiverFromLockedTable(long caregiverId) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate("DELETE FROM CAREGIVER_LOCKED WHERE ID = " + caregiverId);
    }

    /**
     * @param caregiver to be locked
     *
     * @throws SQLException if the caregiver is already locked
     */
    public void lockCaregiver(Caregiver caregiver) throws SQLException {
        TreatmentDAO treatmentDAO = DAOFactory.getDAOFactory().createTreatmentDAO();
        long caregiverId = caregiver.getId();
        List<Treatment> treatments = treatmentDAO.readTreatmentsByCid(caregiverId);
        setCaregiverIdOnTreatmentsToLocked(treatments);
        this.addCaregiverToLockedTable(caregiverId);
    }

    /**
     * @param caregiver to be unlocked
     *
     * @throws SQLException if the caregiver is not locked or the caregiver does not exist in the database
     */
    public void unlockCaregiver(Caregiver caregiver) throws SQLException {
//      Todo: fix being unable to set the previous treatments of this caregiver because there exists no reference
//        possible solution: create another field within the TREATMENTS table that gets set to the last caregiver id when the caregiver gets locked
//        if hes unlocked the current caregiver id gets set to the previous one saved
        long caregiverId = caregiver.getId();
        this.removeCaregiverFromLockedTable(caregiverId);

    }


    /**
     * @param caregiver to be deleted from the database
     *
     * @throws SQLException if an error occurs while deleting the caregiver from the database
     */
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

    /**
     * @param treatments a list of treatments
     */
    private void setCaregiverIdOnTreatmentsToDeleted(List<Treatment> treatments) throws SQLException {
        TreatmentDAO treatmentDAO = DAOFactory.getDAOFactory().createTreatmentDAO();
        for (Treatment treatment : treatments) {
            treatment.setCaregiverId(CaregiverDAO.DELETED_ID);
            treatmentDAO.updateWithoutLastChange(treatment);
        }
    }


    /**
     * @param treatments a list of treatments
     */
    private void setCaregiverIdOnTreatmentsToLocked(List<Treatment> treatments) throws SQLException {
        TreatmentDAO treatmentDAO = DAOFactory.getDAOFactory().createTreatmentDAO();
        for (Treatment treatment : treatments) {
            treatment.setCaregiverId(CaregiverDAO.LOCKED_ID);
            treatmentDAO.updateWithoutLastChange(treatment);
        }
    }
}
