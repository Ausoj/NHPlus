package datastorage;

import java.sql.SQLException;
import java.util.List;

/**
 * @param <T> the type of the object to be stored in the database.
 */
public interface DAO<T> {
    /**
     * @param t the object to be inserted
     * @throws SQLException if an error occurs while inserting the object
     */
    void create(T t) throws SQLException;

    /**
     * @param key the primary key of the object to be fetched
     * @return the object with the given primary key
     * @throws SQLException if the query fails
     */
    T read(long key) throws SQLException;

    /**
     * @return the list of all objects in the database table.
     * @throws SQLException if an error occurs while accessing the database.
     */
    List<T> readAll() throws SQLException;

    /**
     * @param t the object to be updated
     * @throws SQLException if the update fails
     */
    void update(T t) throws SQLException;

    /**
     * @param key the primary key of the object to be deleted
     * @throws SQLException if the deletion fails
     */
    void deleteById(long key) throws SQLException;
}
