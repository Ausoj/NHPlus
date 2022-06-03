package controller.dialogue;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class DialogueController {
    @FXML
    private Text txtHeading;
    @FXML
    private Text txtMessage;
    private Stage stage;


    public void initialize(Stage stage, String heading, String message) {

        this.stage = stage;
        this.txtHeading.setText(heading);
        this.txtMessage.setText(message);
    }

    public void handleOk() {
        stage.close();
    }


}
