package datastorage;

import model.TreatmentType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * This class is used to access the TreatmentType table in the database.
 */
public class TreatmentTypeDAO extends DAOimp<TreatmentType> {
    /**
     * @param conn The connection to the database.
     */
    public TreatmentTypeDAO(Connection conn) {
        super(conn);
    }

    /**
     * @param treatmentType The TreatmentType to be added to the database.
     * @return The query string to add the TreatmentType to the database.
     */
    @Override
    protected String getCreateStatementString(TreatmentType treatmentType) {
        return String.format("INSERT INTO TREATMENT_TYPE (DESCRIPTION) VALUES ('%s')", treatmentType.getDescription());
    }

    /**
     * @param key The primary key of the treatment type to be fetched from the database.
     * @return The query string to fetch the treatment type from the database.
     */
    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * FROM TREATMENT_TYPE WHERE ID = %d", key);
    }

    /**
     * @param result The result set from the database.
     * @return The {@link TreatmentType} from the database.
     */
    @Override
    protected TreatmentType getInstanceFromResultSet(ResultSet result) throws SQLException {
        return new TreatmentType(result.getLong(1), result.getString(2));
    }

    /**
     * @return The query string to fetch all treatment types from the database.
     */
    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM TREATMENT_TYPE";
    }

    /**
     * @param result The result set from the database.
     * @return A list of all treatment types.
     */
    @Override
    protected ArrayList<TreatmentType> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<TreatmentType> list = new ArrayList<>();
        while (result.next()) {
            TreatmentType type = new TreatmentType(result.getLong(1), result.getString(2));
            list.add(type);
        }
        return list;
    }

    /**
     * @param treatmentType The TreatmentType to be updated in the database.
     * @return The query string to update the TreatmentType in the database.
     */
    @Override
    protected String getUpdateStatementString(TreatmentType treatmentType) {
        return String.format("UPDATE TREATMENT_TYPE SET DESCRIPTION = '%s' WHERE ID = %d",
                treatmentType.getDescription(), treatmentType.getId());
    }

    /**
     * @param key The primary key of the treatment type to be deleted from the database.
     * @return The query string to delete the treatment type from the database.
     */
    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("DELETE FROM TREATMENT_TYPE WHERE ID = %d", key);
    }

    /**
     * @param id The primary key of the treatment type to be fetched from the database.
     * @return The description of the treatment type.
     *
     * @throws SQLException If the query fails.
     */
    public String readDescriptionById(long id) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(String.format("SELECT DESCRIPTION FROM TREATMENT_TYPE WHERE ID = %d", id));
        result.next();
        return result.getString(1);
    }

    /**
     * @param description The description of the treatment type to be fetched from the database.
     * @return The primary key of the treatment type.
     *
     * @throws SQLException If the query fails.
     */
    public long readIdByDescription(String description) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(String.format("SELECT ID FROM TREATMENT_TYPE WHERE DESCRIPTION = '%s'", description));
        if (result.next()) {
            return result.getLong(1);
        }
        return -1;
    }

    /**
     * @param id The primary key of the treatment type.
     * @return True if the treatment type is used more than once in the database. False otherwise.
     */
    public boolean isTreatmentTypeUsedMoreThanOnce(long id) {
        Statement st;
        try {
            st = conn.createStatement();
            ResultSet result = st.executeQuery(String.format("SELECT COUNT(TREATMENT_TYPE) FROM TREATMENT WHERE TREATMENT_TYPE = %d", id));
            if (result.next()) {
                return result.getInt(1) > 1;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Deletes unused treatment types from the database.
     * @throws SQLException If the query fails.
     */
    public void deleteUnusedTypes() throws SQLException {
        Statement st = conn.createStatement();
        st.executeQuery("DELETE FROM TREATMENT_TYPE WHERE ID NOT IN (SELECT TREATMENT_TYPE FROM TREATMENT)");
    }

}
