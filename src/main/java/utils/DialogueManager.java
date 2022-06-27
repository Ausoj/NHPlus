package utils;

import javafx.scene.control.Alert;

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
