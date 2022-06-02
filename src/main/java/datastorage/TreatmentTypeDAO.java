package datastorage;

import model.TreatmentType;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class TreatmentTypeDAO extends DAOimp<TreatmentType> {
    public TreatmentTypeDAO(Connection conn) {
        super(conn);
    }

    @Override
    protected String getCreateStatementString(TreatmentType treatmentType) {
        return String.format("INSERT INTO TREATMENT_TYPE (DESCRIPTION) VALUES ('%s')", treatmentType.getDescription());
    }

    @Override
    protected String getReadByIDStatementString(long key) {
        return String.format("SELECT * FROM TREATMENT_TYPE WHERE ID = %d", key);
    }

    @Override
    protected TreatmentType getInstanceFromResultSet(ResultSet result) throws SQLException {
        return new TreatmentType(result.getLong(1), result.getString(2));
    }

    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM TREATMENT_TYPE";
    }

    @Override
    protected ArrayList<TreatmentType> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<TreatmentType> list = new ArrayList<>();
        while (result.next()) {
            TreatmentType type = new TreatmentType(result.getLong(1), result.getString(2));
            list.add(type);
        }
        return list;
    }

    @Override
    protected String getUpdateStatementString(TreatmentType treatmentType) {
        return String.format("UPDATE TREATMENT_TYPE SET ID = %d, DESCRIPTION = %s",
                treatmentType.getId(), treatmentType.getDescription());
    }

    @Override
    protected String getDeleteStatementString(long key) {
        return String.format("DELETE FROM TREATMENT_TYPE WHERE ID = %d", key);
    }

    public String readDescriptionById(long id) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(String.format("SELECT DESCRIPTION FROM TREATMENT_TYPE WHERE ID = %d", id));
        result.next();
        return result.getString(1);
    }

    public long readIdByDescription(String description) throws SQLException {
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(String.format("SELECT ID FROM TREATMENT_TYPE WHERE DESCRIPTION = '%s'", description));
        if (result.next()) {
            return result.getLong(1);
        }
        return -1;
    }

}
