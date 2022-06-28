package datastorage;

import model.Treatment;
import model.TreatmentType;
import utils.DateConverter;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles the database operations for the treatment table.
 */
public class TreatmentDAO extends DAOimp<Treatment> {

    /**
     * @param conn The connection to the database.
     */
    public TreatmentDAO(Connection conn) {
        super(conn);
    }

    /**
     * @param treatment The treatment to be added to the database.
     * @return The query string.
     */
    @Override
    protected String getCreateStatementString(Treatment treatment) {
        return String.format("INSERT INTO treatment (pid, begin, end, TREATMENT_TYPE, remarks, LAST_CHANGE, CAREGIVER_ID) VALUES " +
                        "(%d, %d, %d, '%s', '%s', %d, %d)", treatment.getPatientId(),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getBegin()),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getEnd()),
                treatment.getType().getId(), treatment.getRemarks(), DateConverter.unixTimestampNow(), treatment.getCaregiverId());
    }

    /**
     * @param key the id of the treatment to be fetched.
     * @return The query string.
     */
    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * FROM treatment WHERE tid = %d", key);
    }

    /**
     * @param result The result of the executed query.
     * @return The newly created treatment.
     */
    @Override
    protected Treatment getInstanceFromResultSet(ResultSet result) throws SQLException {
        LocalDate date = DateConverter.convertUnixTimestampToLocalDate(result.getLong(3));
        LocalTime begin = DateConverter.convertUnixTimestampToLocalTime(result.getLong(3));
        LocalTime end = DateConverter.convertUnixTimestampToLocalTime(result.getLong(4));
        return new Treatment(result.getLong(1), result.getLong(2), result.getLong(8),
                date, begin, end, new TreatmentType(result.getLong(5)), result.getString(6));
    }

    /**
     * @return The query string.
     */
    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM TREATMENT WHERE TID NOT IN (SELECT ID FROM TREATMENT_LOCKED)";
    }

    /**
     * @param result The result of the executed query.
     * @return A list of treatments.
     */
    @Override
    protected ArrayList<Treatment> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<>();
        Treatment t;

        while (result.next()) {
            t = getInstanceFromResultSet(result);
            list.add(t);
        }
        return list;
    }

    /**
     * @param treatment The treatment to be updated.
     * @return The query string.
     */
    @Override
    protected String getUpdateStatementString(Treatment treatment) {
        return String.format("UPDATE treatment SET pid = %d, begin = %d, end = '%s'," +
                        "TREATMENT_TYPE = %d, remarks = '%s', LAST_CHANGE = %d, CAREGIVER_ID = %d WHERE tid = %d", treatment.getPatientId(),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getBegin()),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getEnd()),
                treatment.getType().getId(), treatment.getRemarks(), DateConverter.unixTimestampNow(), treatment.getCaregiverId(), treatment.getId());
    }

    /**
     * @param key The id of the treatment to be deleted.
     * @return The query string.
     */
    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("Delete FROM treatment WHERE tid= %d", key);
    }

    /**
     * @param pid The id of the patient.
     * @return A list of all treatments by the provided patient..
     */
    public List<Treatment> readTreatmentsByPid(long pid) throws SQLException {
        ArrayList<Treatment> list;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getReadAllTreatmentsOfOnePatientByPid(pid));
        list = getListFromResultSet(result);
        return list;
    }

    /**
     * @param pid The id of the patient.
     * @return The query string.
     */
    private String getReadAllTreatmentsOfOnePatientByPid(long pid) {
        return String.format("SELECT * FROM treatment WHERE pid = %d", pid);
    }

    /**
     * @param cid The id of the caregiver.
     * @return A list of all treatments by the provided caregiver.
     */
    public List<Treatment> readTreatmentsByCid(long cid) throws SQLException {
        ArrayList<Treatment> list;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getReadAllTreatmentsOfOneCaregiverByCid(cid));
        list = getListFromResultSet(result);
        return list;
    }

    /**
     * @param cid The id of the caregiver.
     * @return The query string.
     */
    private String getReadAllTreatmentsOfOneCaregiverByCid(long cid) {
        return String.format("SELECT * FROM TREATMENT WHERE CAREGIVER_ID = %d", cid);
    }

    /**
     * @param id The id of the treatment to be locked.
     */
    private void lock(long id) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("INSERT INTO TREATMENT_LOCKED (ID) VALUES (%d)", id));
    }

    /**
     * @param id The id of the treatment to be unlocked.
     */
    private void unlock(long id) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate("DELETE FROM TREATMENT_LOCKED WHERE ID = " + id);
    }

    /**
     * @param key the id of the patient to delete his treatments.
     */
    public void deleteByPid(long key) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("Delete FROM treatment WHERE pid= %d", key));
    }

    /**
     * @param treatment The treatment to be locked.
     */
    public void lockTreatment(Treatment treatment) {
        try {
            this.lock(treatment.getId());
        } catch (SQLIntegrityConstraintViolationException ignore) {

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param treatment The treatment to be unlocked.
     */
    public void unlockTreatment(Treatment treatment) {
        try {
            this.unlock(treatment.getId());
        } catch (SQLIntegrityConstraintViolationException ignore) {

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param treatment The treatment to be deleted from the database.
     */
    public void deleteTreatment(Treatment treatment) {
        TreatmentTypeDAO tDAO = DAOFactory.getDAOFactory().createTreatmentTypeDAO();
        try {
            this.unlock(treatment.getId());
            this.deleteById(treatment.getId());
            tDAO.deleteById(treatment.getType().getId());
        } catch (SQLIntegrityConstraintViolationException ignored) {

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param treatment The treatment to be updated without changing its last change date.
     */
    public void updateWithoutLastChange(Treatment treatment) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("UPDATE treatment SET pid = %d, begin = %d, end = '%s'," +
                        "TREATMENT_TYPE = %d, remarks = '%s', CAREGIVER_ID = %d WHERE tid = %d", treatment.getPatientId(),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getBegin()),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getEnd()),
                treatment.getType().getId(), treatment.getRemarks(), treatment.getCaregiverId(), treatment.getId()));
    }

    /**
     * @param unixTime A unix timestamp.
     * @return A {@link ResultSet} with all treatments that have not been changed since the provided timestamp.
     */
    public ResultSet getAllTreatmentsWithoutChangeSince(long unixTime) throws SQLException {
        Statement st = conn.createStatement();
        return st.executeQuery("SELECT TID\n" +
                "FROM TREATMENT\n" +
                "WHERE LAST_CHANGE < " + unixTime);
    }
}