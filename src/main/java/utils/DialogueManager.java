package utils;

import controller.Main;
import controller.dialogue.DialogueController;
import enums.DialogueType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public final class DialogueManager {

    private static DialogueManager instance;

    private DialogueManager() {
        instance = this;
    }

    public static DialogueManager getInstance() {
        if (instance == null) {
            new DialogueManager();
        }
        return instance;
    }

    public void showAlert(String heading, Exception e) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Achtung");
        alert.setHeaderText(heading);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
