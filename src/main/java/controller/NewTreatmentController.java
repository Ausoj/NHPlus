package controller;

import datastorage.CaregiverDAO;
import datastorage.DAOFactory;
import datastorage.TreatmentDAO;
import datastorage.TreatmentTypeDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Caregiver;
import model.Patient;
import model.Treatment;
import model.TreatmentType;
import org.controlsfx.control.textfield.TextFields;
import utils.DateConverter;
import utils.DialogueManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NewTreatmentController {
    @FXML
    private Label lblSurname;
    @FXML
    private Label lblFirstname;
    @FXML
    private TextField txtBegin;
    @FXML
    private TextField txtEnd;
    @FXML
    private TextField txtDescription;
    @FXML
    private TextArea taRemarks;
    @FXML
    private DatePicker datepicker;
    @FXML
    private ComboBox<String> comboCaregiver;

    private AllTreatmentController controller;
    private Patient patient;
    private Stage stage;

    private List<Caregiver> allCaregivers;

    public void initialize(AllTreatmentController controller, Stage stage, Patient patient) {
        this.controller = controller;
        this.patient = patient;
        this.stage = stage;
        showPatientData();
        populateDescriptionTextField();
        populateCaregiverCombobox();
//        Todo: prefill Datefield with current date
    }

    private void showPatientData() {
        this.lblFirstname.setText(patient.getFirstName());
        this.lblSurname.setText(patient.getSurname());
    }

    @FXML
    public void handleAdd() {
        String s_begin = txtBegin.getText();
        String remarks = taRemarks.getText();
        try {
            Caregiver caregiver = searchInList(comboCaregiver.getSelectionModel().getSelectedItem());
            LocalDate date = this.datepicker.getValue();
            if (date == null) throw new IllegalArgumentException("Bitte wähle ein Datum über den Kalender aus!");

            LocalTime begin = DateConverter.convertStringToLocalTime(txtBegin.getText());
            LocalTime end = DateConverter.convertStringToLocalTime(txtEnd.getText());
            TreatmentType type = new TreatmentType(txtDescription.getText());
            Treatment treatment = new Treatment(patient.getId(), caregiver.getId(), date,
                    begin, end, type, remarks);
            createTreatment(treatment);
            CaregiverDAO caregiverDAO = DAOFactory.getDAOFactory().createCaregiverDAO();
            caregiverDAO.setLastTreatment(caregiverDAO.read(treatment.getCaregiverId()));
            controller.readAllAndShowInTableView();
            stage.close();
        } catch (IllegalArgumentException | SQLException e) {
            DialogueManager.getInstance().showAlert("Behandlung konnte nicht angelegt werden", e);
        }
    }

    public void populateCaregiverCombobox() {
        CaregiverDAO caregiverDAO = DAOFactory.getDAOFactory().createCaregiverDAO();
        ObservableList<String> allCaregiversNames = FXCollections.observableArrayList();
        try {
            allCaregivers = caregiverDAO.readAll();
            for (Caregiver caregiver : allCaregivers) {
                allCaregiversNames.add(caregiver.getAbbreviatedName());
            }
            comboCaregiver.setItems(allCaregiversNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void populateDescriptionTextField() {
        TreatmentTypeDAO dao = DAOFactory.getDAOFactory().createTreatmentTypeDAO();
        List<TreatmentType> allTreatmentTypes;
        List<String> treatmentTypes = new ArrayList<>();
        try {
            allTreatmentTypes = dao.readAll();
            for (TreatmentType type : allTreatmentTypes) {
                treatmentTypes.add(type.getDescription());
            }
            TextFields.bindAutoCompletion(txtDescription, treatmentTypes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Caregiver searchInList(String formattedName) {
        try {
            for (Caregiver caregiver : allCaregivers) {
                if (formattedName.equals(caregiver.getAbbreviatedName())) {
                    return caregiver;
                }
            }
        } catch (NullPointerException e) {
            throw new IllegalArgumentException("Es muss ein Pfleger ausgewählt werden.");
        }
        return null;
    }

    private void createTreatment(Treatment treatment) {
        TreatmentDAO dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        try {
            dao.create(treatment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancel() {
        stage.close();
    }
}