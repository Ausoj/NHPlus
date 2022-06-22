package datastorage;

import model.Treatment;
import model.TreatmentType;
import utils.DateConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class TreatmentDAO extends DAOimp<Treatment> {

    public TreatmentDAO(Connection conn) {
        super(conn);
    }

    @Override
    protected String getCreateStatementString(Treatment treatment) {
        return String.format("INSERT INTO treatment (pid, begin, end, TREATMENT_TYPE, remarks, LAST_CHANGE, CAREGIVER_ID) VALUES " +
                        "(%d, %d, %d, '%s', '%s', %d, %d)", treatment.getPatientId(),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getBegin()),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getEnd()),
                treatment.getType().getId(), treatment.getRemarks(), DateConverter.unixTimestampNow(), treatment.getCaregiverId());
    }

    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * FROM treatment WHERE tid = %d", key);
    }

    @Override
    protected Treatment getInstanceFromResultSet(ResultSet result) throws SQLException {
        LocalDate date = DateConverter.convertUnixTimestampToLocalDate(result.getLong(3));
        LocalTime begin = DateConverter.convertUnixTimestampToLocalTime(result.getLong(3));
        LocalTime end = DateConverter.convertUnixTimestampToLocalTime(result.getLong(4));
        return new Treatment(result.getLong(1), result.getLong(2), result.getLong(8),
                date, begin, end, new TreatmentType(result.getLong(5)), result.getString(6));
    }

    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM TREATMENT WHERE TID NOT IN (SELECT ID FROM TREATMENT_LOCKED)";
    }

    @Override
    protected ArrayList<Treatment> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment t = null;

        while (result.next()) {
            t = getInstanceFromResultSet(result);
            list.add(t);
        }
        return list;
    }

    @Override
    protected String getUpdateStatementString(Treatment treatment) {
        return String.format("UPDATE treatment SET pid = %d, begin = %d, end = '%s'," +
                        "TREATMENT_TYPE = %d, remarks = '%s', LAST_CHANGE = %d, CAREGIVER_ID = %d WHERE tid = %d", treatment.getPatientId(),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getBegin()),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getEnd()),
                treatment.getType().getId(), treatment.getRemarks(), DateConverter.unixTimestampNow(), treatment.getCaregiverId(), treatment.getId());
    }

    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("Delete FROM treatment WHERE tid= %d", key);
    }

    public List<Treatment> readTreatmentsByPid(long pid) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment object = null;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getReadAllTreatmentsOfOnePatientByPid(pid));
        list = getListFromResultSet(result);
        return list;
    }

    private String getReadAllTreatmentsOfOnePatientByPid(long pid) {
        return String.format("SELECT * FROM treatment WHERE pid = %d", pid);
    }

    public List<Treatment> readTreatmentsByCid(long cid) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment object = null;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getReadAllTreatmentsOfOneCaregiverByCid(cid));
        list = getListFromResultSet(result);
        return list;
    }

    private String getReadAllTreatmentsOfOneCaregiverByCid(long cid) {
        return String.format("SELECT * FROM TREATMENT WHERE CAREGIVER_ID = %d", cid);
    }

    public void lock(long id) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("INSERT INTO TREATMENT_LOCKED (ID) VALUES (%d)", id));
    }

    public void deleteByPid(long key) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("Delete FROM treatment WHERE pid= %d", key));
    }

    public void updateWithoutLastChange(Treatment treatment) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("UPDATE treatment SET pid = %d, begin = %d, end = '%s'," +
                        "TREATMENT_TYPE = %d, remarks = '%s', CAREGIVER_ID = %d WHERE tid = %d", treatment.getPatientId(),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getBegin()),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getEnd()),
                treatment.getType().getId(), treatment.getRemarks(), treatment.getCaregiverId(), treatment.getId()));
    }

}