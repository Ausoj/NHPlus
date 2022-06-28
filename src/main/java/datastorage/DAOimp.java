package datastorage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract class that is used to access the database and retrieve data from it.
 *
 * @param <T> The type of the object to be added to the database.
 */
public abstract class DAOimp<T> implements DAO<T> {
    /**
     * The connection to the database.
     */
    protected final Connection conn;

    /**
     * @param conn The connection to the database.
     */
    public DAOimp(Connection conn) {
        this.conn = conn;
    }

    /**
     * Executes the create query on the database.
     *
     * @param t the object to be inserted
     */
    @Override
    public void create(T t) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(getCreateStatementString(t));
    }

    /**
     * Executes the readById query on the database.
     *
     * @param key the primary key of the object to be fetched
     * @return the object with the given primary key
     */
    @Override
    public T read(long key) throws SQLException {
        T object = null;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getReadByIDStatementString(key));
        if (result.next()) {
            object = getInstanceFromResultSet(result);
        }
        return object;
    }

    /**
     * Executes the readAll query on the database.
     *
     * @return the list of all objects in the database
     */
    @Override
    public List<T> readAll() throws SQLException {
        ArrayList<T> list;
        Statement st = conn.createStatement();
        ResultSet result = st.executeQuery(getReadAllStatementString());
        list = getListFromResultSet(result);
        return list;
    }

    /**
     * Executes the update query on the database.
     *
     * @param t the object to be updated
     */
    @Override
    public void update(T t) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(getUpdateStatementString(t));
    }

    /**
     * Executes the deleteById query on the database.
     *
     * @param key the primary key of the object to be deleted
     */
    @Override
    public void deleteById(long key) throws SQLException {
        Statement st = conn.createStatement();
        st.executeUpdate(getDeleteStatementString(key));
    }

    /**
     * @param t the object to be inserted
     * @return the create query string
     */
    protected abstract String getCreateStatementString(T t);

    /**
     * @param key the id of the object to be fetched
     * @return the readById query string
     */
    protected abstract String getReadByIDStatementString(long key);

    /**
     * @param set the result set from the database
     * @return the object from the result set
     * @throws SQLException if the query fails
     */
    protected abstract T getInstanceFromResultSet(ResultSet set) throws SQLException;

    /**
     * @return the readAll query string
     */
    protected abstract String getReadAllStatementString();

    /**
     * @param set the result set from the database
     * @return the list of objects generated from the result set
     * @throws SQLException if the query fails
     */
    protected abstract ArrayList<T> getListFromResultSet(ResultSet set) throws SQLException;

    /**
     * @param t the object to be updated
     * @return the update query string
     */
    protected abstract String getUpdateStatementString(T t);

    /**
     * @param key the id of the object to be deleted
     * @return the deletion query string
     */
    protected abstract String getDeleteStatementString(long key);
}
