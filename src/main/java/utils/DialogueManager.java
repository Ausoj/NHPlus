package utils;

import controller.Main;
import controller.dialogue.DialogueController;
import enums.DialogueType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

    public void open(DialogueType type, String heading, String message) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/dialogue/" + type.toString() + ".fxml"));
            DialogPane pane = loader.load();
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Application.css")).toExternalForm());
            Stage stage = new Stage();

            DialogueController dialogueController = loader.getController();
            dialogueController.initialize(stage, heading, message);

            System.out.println(heading);
            System.out.println(message);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
