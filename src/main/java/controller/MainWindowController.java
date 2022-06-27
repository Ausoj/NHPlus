package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;

import java.io.IOException;

/**
 * The <code>MainWindowController</code> is the controller for the main window.
 * It is mainly responsible for the event handling of the button panel on the left side of the application.
 */
public class MainWindowController {

    @FXML
    private BorderPane mainBorderPane;

    /**
     * This method is called when the user clicks the "Patient/innen" button.
     * Afterwards the AllPatientView is loaded and displayed.
     */
    @FXML
    private void handleShowAllPatient() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/AllPatientView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        AllPatientController controller = loader.getController();
    }

    /**
     * This method is called when the user clicks the "Pfleger/innen" button.
     * Afterwards the AllCaregiverView is loaded and displayed.
     */
    @FXML
    private void handleShowAllCaregiver() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/AllCaregiverView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        AllCaregiverController controller = loader.getController();
    }

    /**
     * This method is called when the user clicks the "Behandlungen" button.
     * Afterwards the AllTreatmentView is loaded and displayed.
     */
    @FXML
    private void handleShowAllTreatments() {
        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/AllTreatmentView.fxml"));
        try {
            mainBorderPane.setCenter(loader.load());
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        AllTreatmentController controller = loader.getController();
    }
}
