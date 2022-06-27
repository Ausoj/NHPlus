package controller;

import datastorage.DAOFactory;
import datastorage.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * The <code>LoginController</code> is the controller for the LoginView.
 */
public class LoginController {

    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField userName;


    /**
     * This method handles the event when the user clicks the login button.
     * It checks if:
     * <ul>
     *     <li>The user name is not empty</li>
     *     <li>The password is not empty</li>
     *     <li>The username exists in the database</li>
     *     <li>The entered password corresponds to the entered username</li>
     * </ul>
     * If all of the above is true, the user is logged in and the main view is loaded.
     * If not, an error message regarding the specific error will be displayed.
     */
    public void handleSubmitButtonAction() {
        if (loginFieldsAreEmpty()) {
            noInputAlert();
            return;
        }

        UserDAO userDAO = DAOFactory.getDAOFactory().createUserDAO();
        List<User> users;
        try {
            users = userDAO.readAll();
            User searchForUser = null;
            for (User user : users) {
                if (user.getUsername().equals(userName.getText())) {
                    searchForUser = user;
                    break;
                }
            }
            if (searchForUser == null) {
                userNameAlert();
                return;
            }
            if (!searchForUser.getPassword().equals(passwordField.getText())) {
                passwordAlert();
                return;
            }

            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/MainWindowView.fxml"));
            try {
                Main.primaryStage.setScene(new Scene(loader.load()));
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    /**
     * Checks whether the userName and password fields are empty.
     *
     * @return true if the userName and password fields are empty, false otherwise
     */
    private boolean loginFieldsAreEmpty() {
        return userName.getText().isEmpty() && passwordField.getText().isEmpty();
    }

    /**
     * Displays an error message if the userName is not found in the database.
     */
    public void userNameAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler!");
        alert.setHeaderText("Falscher Username!");
        alert.setContentText("Der angegebene Username konnte nicht gefunden werden!");
        alert.showAndWait();
    }

    /**
     * Displays an error message if the password is not correct.
     */
    public void passwordAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler!");
        alert.setHeaderText("Falsches Passwort!");
        alert.setContentText("Das angegebene Passwort ist falsch!");
        alert.showAndWait();
    }

    /**
     * Displays an error message if the userName and password fields are empty.
     */
    public void noInputAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler!");
        alert.setHeaderText("Keine Daten!");
        alert.setContentText("Es wurden nicht alle Felder mit Daten befüllt!");
        alert.showAndWait();
    }

}
