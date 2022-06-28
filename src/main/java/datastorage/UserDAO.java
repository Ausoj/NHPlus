package datastorage;

import model.User;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * This class is used to access the User table in the database.
 */
public class UserDAO extends DAOimp<User> {
    /**
     * @param conn The connection to the database.
     */
    public UserDAO(Connection conn) {
        super(conn);
    }

    /**
     * @param user The user to be added to the database.
     * @return The query string to add the user to the database.
     */
    @Override
    protected String getCreateStatementString(User user) {
        return String.format("INSERT INTO user (username, password) VALUES ('%s', '%s')",
                user.getUsername(), user.getPassword());
    }

    @Override
    protected String getReadByIDStatementString(long id) {
        return null;
    }

    /**
     * @param result The result set from the database.
     * @return The {@link User} from the result set.
     */
    @Override
    protected User getInstanceFromResultSet(ResultSet result) throws SQLException {
        User p;
        p = new User(result.getString(1), result.getString(2));
        return p;
    }

    /**
     * @return The query string to get all users from the database.
     */
    @Override
    protected String getReadAllStatementString() {
        return "SELECT * FROM user";
    }

    /**
     * @param result The result set from the database.
     * @return The list of {@link User} from the result set.
     */
    @Override
    protected ArrayList<User> getListFromResultSet(ResultSet result) throws SQLException {
        ArrayList<User> list = new ArrayList<>();
        User p;
        while (result.next()) {
            p = new User(result.getString(1), result.getString(2));
            list.add(p);
        }
        return list;
    }

    /**
     * @param user The user to be updated in the database.
     * @return The query string to update the user in the database.
     */
    @Override
    protected String getUpdateStatementString(User user) {
        return String.format("UPDATE user SET username = '%s', password = '%s'", user.getUsername(), user.getPassword());
    }

    /**
     * @param username The username of the user to be deleted from the database.
     * @return The query string to delete the user from the database.
     */
    @Override
    protected String getDeleteStatementString(long username) {
        return String.format("Delete FROM patient WHERE username=%d", username);
    }
}