package model;

/**
 * This class represents a user.
 */
public class User {
    private final String username;
    private final String password;


    /**
     * @param username the username of the user
     * @param password the password of the user
     */
    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * @return the username of the user
     */
    public String getUsername() {
        return username;
    }

    /**
     * @return the password of the user
     */
    public String getPassword() {
        return password;
    }
}
