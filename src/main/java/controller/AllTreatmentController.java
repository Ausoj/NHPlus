package controller;

import datastorage.CaregiverDAO;
import datastorage.DAOFactory;
import datastorage.PatientDAO;
import datastorage.TreatmentDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.Caregiver;
import model.Patient;
import model.Treatment;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * The <code>AllTreatmentController</code> is the controller for the AllTreatmentView.
 */
public class AllTreatmentController {
    @FXML
    private TableView<Treatment> tableView;
    @FXML
    private TableColumn<Treatment, Integer> colID;
    @FXML
    private TableColumn<Treatment, Integer> colPid;
    @FXML
    private TableColumn<Treatment, String> colDate;
    @FXML
    private TableColumn<Treatment, String> colBegin;
    @FXML
    private TableColumn<Treatment, String> colEnd;
    @FXML
    private TableColumn<Treatment, String> colDescription;
    @FXML
    private TableColumn<Treatment, String> colCaregiver;
    @FXML
    private ComboBox<String> comboBox;

    private final ObservableList<Treatment> tableviewContent =
            FXCollections.observableArrayList();
    private TreatmentDAO dao;
    private final ObservableList<String> myComboBoxData =
            FXCollections.observableArrayList();
    private ArrayList<Patient> patientList;

    /**
     * Initializes the controller class. This method is automatically called once the fxml file has been loaded.
     */
    public void initialize() {
        readAllAndShowInTableView();
        CaregiverDAO caregiverDAO = DAOFactory.getDAOFactory().createCaregiverDAO();

        comboBox.setItems(myComboBoxData);
        comboBox.getSelectionModel().select(0);

        this.colID.setCellValueFactory(new PropertyValueFactory<>("id"));
        this.colPid.setCellValueFactory(new PropertyValueFactory<>("patientId"));
        this.colDate.setCellValueFactory(new PropertyValueFactory<>("date"));
        this.colBegin.setCellValueFactory(new PropertyValueFactory<>("begin"));
        this.colEnd.setCellValueFactory(new PropertyValueFactory<>("end"));
        this.colDescription.setCellValueFactory(data -> data.getValue().getType().descriptionProperty());
        this.colCaregiver.setCellValueFactory(data -> {
            try {
                Caregiver c = caregiverDAO.read(data.getValue().getCaregiverId());
                return new SimpleStringProperty(String.format("%-15s %s", c.getAbbreviatedName(), c.getPhoneNumber()));

            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        });
        this.tableView.setItems(this.tableviewContent);
        createComboBoxData();
    }

    /**
     * Calls the {@link TreatmentDAO#readAll()} method and adds the returned treatments to the tableview.
     */
    public void readAllAndShowInTableView() {
        comboBox.getSelectionModel().select(0);
        this.tableviewContent.clear();
        this.dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        List<Treatment> allTreatments;
        try {
            allTreatments = dao.readAll();
            this.tableviewContent.addAll(allTreatments);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Creates the data for the combobox regarding the patients.
     */
    private void createComboBoxData() {
        PatientDAO dao = DAOFactory.getDAOFactory().createPatientDAO();
        try {
            patientList = (ArrayList<Patient>) dao.readAll();
            this.myComboBoxData.add("alle");
            for (Patient patient : patientList) {
                this.myComboBoxData.add(patient.getSurname());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * Handles the event when the user interacts with the combobox.
     * Clears the current tableview and calls the respective method in {@link TreatmentDAO} to fetch the right treatments.
     */
    @FXML
    public void handleComboBox() {
        String p = this.comboBox.getSelectionModel().getSelectedItem();
        this.tableviewContent.clear();
        this.dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        List<Treatment> allTreatments;
        if (p.equals("alle")) {
            try {
                allTreatments = this.dao.readAll();
                this.tableviewContent.addAll(allTreatments);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        Patient patient = searchInList(p);
        if (patient != null) {
            try {
                allTreatments = dao.readTreatmentsByPid(patient.getId());
                this.tableviewContent.addAll(allTreatments);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *  Searches for a specific surname within the list of patients.
     *
     * @param surname the surname of the patient to search for.
     * @return the patient that was searched or null if the patient was not found.
     */
    private Patient searchInList(String surname) {
        for (Patient patient : this.patientList) {
            if (patient.getSurname().equals(surname)) {
                return patient;
            }
        }
        return null;
    }

    /**
     * Handles the event when the user clicks the button to lock a treatment.
     * Displays a dialog to ask the user if he really wants to lock the treatment.
     * Calls the {@link TreatmentDAO#lockTreatment(Treatment)} method if the dialogue was confirmed.
     */
    @FXML
    public void handleLock() {
        TreatmentDAO dao = DAOFactory.getDAOFactory().createTreatmentDAO();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Behandlung sperren");
        alert.setHeaderText("Die ausgewählte Behandlung wird gesperrt.\n" +
                "Möchten Sie die Behandlung sperren?");
        alert.setContentText("Die Behandlung wird gesperrt.");
        Optional<ButtonType> choice = alert.showAndWait();
        if (!choice.get().getText().equals("OK")) return;

        int index = this.tableView.getSelectionModel().getSelectedIndex();
        Treatment t = this.tableviewContent.remove(index);
        dao.lockTreatment(t);
    }

    /**
     * Handles the event when the user clicks the button to delete a treatment.
     * Displays a dialog to ask the user if he really wants to delete the treatment.
     * Calls the {@link TreatmentDAO#deleteTreatment(Treatment)} method if the dialogue was confirmed.
     */
    @FXML
    public void handleDelete() {
        TreatmentDAO dao = DAOFactory.getDAOFactory().createTreatmentDAO();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Behandlung löschen");
        alert.setHeaderText("Die ausgewählte Behandlung wird gelöscht.\n" +
                "Möchten Sie die Behandlung wirklich löschen?");
        alert.setContentText("Die Behandlung wird endgültig gelöscht.");
        Optional<ButtonType> choice = alert.showAndWait();
        if (!choice.get().getText().equals("OK")) return;

        int index = this.tableView.getSelectionModel().getSelectedIndex();
        Treatment t = this.tableviewContent.remove(index);
        dao.deleteTreatment(t);
    }

    /**
     * Handles the event when the user clicks the button to add a new treatment.
     * Displays an informational dialog if the user has not selected a patient.
     */
    @FXML
    public void handleNewTreatment() {
        try {
            String p = this.comboBox.getSelectionModel().getSelectedItem();
            Patient patient = searchInList(p);
            newTreatmentWindow(patient);
        } catch (NullPointerException e) {
//            Todo: Remove own DialogueManager and use this one
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Information");
            alert.setHeaderText("Patient für die Behandlung fehlt!");
            alert.setContentText("Wählen Sie über die Combobox einen Patienten aus!");
            alert.showAndWait();

        }
    }

    /**
     * Handles the event when the user clicks on a specific cell in the tableview.
     */
    @FXML
    public void handleMouseClick() {
        tableView.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2 && (tableView.getSelectionModel().getSelectedItem() != null)) {
                int index = this.tableView.getSelectionModel().getSelectedIndex();
                Treatment treatment = this.tableviewContent.get(index);

                treatmentWindow(treatment);
            }
        });
    }

    /**
     * Opens another window on top of the current one to add a new treatment.
     *
     * @param patient the patient to whom the treatment belongs to.
     */
    public void newTreatmentWindow(Patient patient) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/NewTreatmentView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Application.css")).toExternalForm());
            //da die primaryStage noch im Hintergrund bleiben soll
            Stage stage = new Stage();
            NewTreatmentController controller = loader.getController();
            controller.initialize(this, stage, patient);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens another window on top of the current one to edit the selected treatment.
     *
     * @param treatment the treatment to be edited in the newly created window.
     */
    public void treatmentWindow(Treatment treatment) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/TreatmentView.fxml"));
            AnchorPane pane = loader.load();
            Scene scene = new Scene(pane);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Application.css")).toExternalForm());
            //da die primaryStage noch im Hintergrund bleiben soll
            Stage stage = new Stage();
            TreatmentController controller = loader.getController();

            controller.initializeController(this, stage, treatment);

            stage.setScene(scene);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}