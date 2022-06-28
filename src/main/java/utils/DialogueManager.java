package utils;

import javafx.scene.control.Alert;

/**
 * This is a utility singleton class to display dialogues to the user.
 */
public final class DialogueManager {

    private static DialogueManager instance;

    private DialogueManager() {
        instance = this;
    }

    /**
     * @return the instance of the DialogueManager
     */
    public static DialogueManager getInstance() {
        if (instance == null) {
            new DialogueManager();
        }
        return instance;
    }

    /**
     * @param heading the heading of the dialogue
     * @param e       the exception to display
     */
    public void showAlert(String heading, Exception e) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Achtung");
        alert.setHeaderText(heading);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
    }
}
