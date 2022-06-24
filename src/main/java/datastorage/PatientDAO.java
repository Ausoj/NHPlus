package datastorage;

import model.Caregiver;
import model.Patient;
import model.Treatment;
import utils.DateConverter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Interface <code>DAOImp</code>. Overrides methods to generate specific patient-SQL-queries.
 */
public class PatientDAO extends DAOimp<Patient> {

    /**
     * constructs Onbject. Calls the Constructor from <code>DAOImp</code> to store the connection.
     *
     * @param conn
     */
    public PatientDAO(Connection conn) {
        super(conn);
    }

    /**
     * generates a <code>INSERT INTO</code>-Statement for a given patient
     *
     * @param patient for which a specific INSERT INTO is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getCreateStatementString(Patient patient) {
        return String.format("INSERT INTO PATIENT (PERSON_ID, CARE_LEVEL, ROOM_NUMBER) VALUES (%d, '%s', '%s');",
                patient.getPersonId(), patient.getCareLevel(), patient.getRoomnumber());
    }

    /**
     * generates a <code>select</code>-Statement for a given key
     *
     * @param key for which a specific SELECTis to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT PATIENT.ID, PERSON.FIRSTNAME, PERSON.SURNAME, PERSON.DATE_OF_BIRTH, PATIENT.CARE_LEVEL, PATIENT.ROOM_NUMBER\n" +
                "FROM PATIENT\n" +
                "JOIN PERSON on PERSON.ID = PATIENT.PERSON_ID\n" +
                "WHERE PATIENT.ID = %d", key);
    }

    /**
     * maps a <code>ResultSet</code> to a <code>Patient</code>
     *
     * @param result ResultSet with a single row. Columns will be mapped to a patient-object.
     * @return patient with the data from the resultSet.
     */
    @Override
    protected Patient getInstanceFromResultSet(ResultSet result) throws SQLException {
        Patient p = null;
        LocalDate date = DateConverter.convertUnixTimestampToLocalDate(result.getLong(4));
        p = new Patient(result.getInt(1), result.getString(2),
                result.getString(3), date, result.getString(5),
                result.getString(6));
        return p;
    }

    /**
     * generates a <code>SELECT</code>-Statement for all patients.
     *
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getReadAllStatementString() {
        return "SELECT PATIENT.ID, PERSON.FIRSTNAME, PERSON.SURNAME, PERSON.DATE_OF_BIRTH, PATIENT.CARE_LEVEL, PATIENT.ROOM_NUMBER\n" +
                "FROM PATIENT\n" +
                "JOIN PERSON on PERSON.ID = PATIENT.PERSON_ID\n";
    }

    /**
     * maps a <code>ResultSet</code> to a <code>Patient-List</code>
     *
     * @param result ResultSet with a multiple rows. Data will be mapped to patient-object.
     * @return ArrayList with patients from the resultSet.
     */
    @Override
    protected ArrayList<Patient> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<Patient> list = new ArrayList<Patient>();
        Patient p = null;
        while (result.next()) {
            LocalDate date = DateConverter.convertUnixTimestampToLocalDate(result.getLong(4));
            p = new Patient(result.getInt(1), result.getString(2),
                    result.getString(3), date,
                    result.getString(5), result.getString(6));
            list.add(p);
        }
        return list;
    }

    /**
     * generates a <code>UPDATE</code>-Statement for a given patient
     *
     * @param patient for which a specific update is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getUpdateStatementString(Patient patient) {
        return String.format("UPDATE PATIENT SET CARE_LEVEL = '%s', ROOM_NUMBER = '%s' WHERE ID = %d;",
                patient.getCareLevel(), patient.getRoomnumber(), patient.getId());
    }

    /**
     * generates a <code>delete</code>-Statement for a given key
     *
     * @param key for which a specific DELETE is to be created
     * @return <code>String</code> with the generated SQL.
     */
    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("DELETE FROM PATIENT WHERE ID = %d;", key);
    }

    public ResultSet getAllPatientIdsWithoutTreatmentSince(long unixTime) throws SQLException {
        Statement st = conn.createStatement();
        return st.executeQuery("SELECT PATIENT.ID\n" +
                "FROM PATIENT\n" +
                "JOIN TREATMENT on PATIENT.ID = TREATMENT.PID\n" +
                "GROUP BY PATIENT.ID\n" +
                "HAVING MAX(LAST_CHANGE) < " + unixTime);
    }

    public void deletePatient(Patient patient) throws SQLException {
        PersonDAO personDAO = DAOFactory.getDAOFactory().createPersonDAO();
        long caregiverId = patient.getId();
        unlockPatient(patient);
        this.deleteById(caregiverId);
        personDAO.deleteById(patient.getPersonId());
    }

    public void lockPatient(Patient patient) throws SQLException {
        long patientId = patient.getId();
        this.addPatientToLockedTable(patientId);
    }

    public void unlockPatient(Patient patient) throws SQLException {
        long patientId = patient.getId();
        this.removePatientFromLockedTable(patientId);

    }

    private void addPatientToLockedTable(long id) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate("INSERT INTO PATIENT_LOCKED (ID) VALUES (" + id + ")");
    }

    private void removePatientFromLockedTable(long id) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate("DELETE FROM PATIENT_LOCKED WHERE ID = " + id);
    }
}
