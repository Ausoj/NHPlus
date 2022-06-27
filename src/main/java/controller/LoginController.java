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

public class LoginController {

    @FXML
    public PasswordField passwordField;
    @FXML
    public TextField userName;


    public void handleSubmitButtonAction() {
        if (!userName.getText().isEmpty() && !passwordField.getText().isEmpty()) {
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
                if (searchForUser != null) {
                    if (searchForUser.getPassword().equals(passwordField.getText())) {
                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/MainWindowView.fxml"));
                        try {
                            Main.primaryStage.setScene(new Scene(loader.load()));
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    } else {
                        passwordAlert();
                    }
                } else {
                    userNameAlert();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            noInputAlert();
        }

    }

    public void userNameAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler!");
        alert.setHeaderText("Falscher Username!");
        alert.setContentText("Der angegebene Username konnte nicht gefunden werden!");
        alert.showAndWait();
    }

    public void passwordAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler!");
        alert.setHeaderText("Falsches Passwort!");
        alert.setContentText("Das angegebene Passwort ist falsch!");
        alert.showAndWait();
    }

    public void noInputAlert() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fehler!");
        alert.setHeaderText("Keine Daten!");
        alert.setContentText("Es wurden nicht alle Felder mit Daten befüllt!");
        alert.showAndWait();
    }

}
