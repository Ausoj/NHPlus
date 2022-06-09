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
        return String.format("INSERT INTO treatment (pid, begin, end, TREATMENT_TYPE, remarks) VALUES " +
                        "(%d, %d, %d, '%s', '%s')", treatment.getPid(),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getBegin()),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getEnd()),
                treatment.getType().getId(), treatment.getRemarks());
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
        return new Treatment(result.getLong(1), result.getLong(2),
                date, begin, end, new TreatmentType(result.getLong(5)), result.getString(6));
    }

    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM TREATMENT";
    }

    @Override
    protected ArrayList<Treatment> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Treatment> list = new ArrayList<Treatment>();
        Treatment t = null;
        while (result.next()) {
//            Todo: exchange logic with call to getInstanceFromResultSet() above
            LocalDate date = DateConverter.convertUnixTimestampToLocalDate(result.getLong(3));
            LocalTime begin = DateConverter.convertUnixTimestampToLocalTime(result.getLong(3));
            LocalTime end = DateConverter.convertUnixTimestampToLocalTime(result.getLong(4));
            t = new Treatment(result.getLong(1), result.getLong(2),
                    date, begin, end, new TreatmentType(result.getLong(5)), result.getString(6));
            list.add(t);
        }
        return list;
    }

    @Override
    protected String getUpdateStatementString(Treatment treatment) {
//        Todo: swap out treatment__type %s with %d if its not making a difference since treatment_type id is long
        return String.format("UPDATE treatment SET pid = %d, begin = %d, end = '%s'," +
                        "TREATMENT_TYPE = '%s', remarks = '%s' WHERE tid = %d", treatment.getPid(),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getBegin()),
                DateConverter.convertStringToUnixTimestamp(treatment.getDate(), treatment.getEnd()),
                treatment.getType().getId(), treatment.getRemarks(), treatment.getTid());
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

    public void deleteByPid(long key) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(String.format("Delete FROM treatment WHERE pid= %d", key));
    }
}