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

/**
 * The <code>NewTreatmentController</code> is the controller for the NewTreatmentView.
 * It handles the gathering of data from the user through the input fields and creates the corresponding treatment.
 */
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

    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     *
     * @param controller The controller of the AllTreatmentView.
     * @param stage The previous stage.
     * @param patient The patient for which the treatment is to be created.
     */
    public void initialize(AllTreatmentController controller, Stage stage, Patient patient) {
        this.controller = controller;
        this.patient = patient;
        this.stage = stage;
        showPatientData();
        populateDescriptionTextField();
        populateCaregiverCombobox();
//        Todo: prefill Date field with current date
    }

    /**
     * Shows the patients' data in the labels.
     */
    private void showPatientData() {
        this.lblFirstname.setText(patient.getFirstName());
        this.lblSurname.setText(patient.getSurname());
    }

    /**
     * Handles the creation of a new treatment.
     * It checks if the input is valid and creates the treatment if it is.
     * Otherwise, it displays an error message.
     *
     */
    @FXML
    public void handleAdd() {
        String remarks = taRemarks.getText();
        try {
            Caregiver caregiver = searchInList(comboCaregiver.getSelectionModel().getSelectedItem());
            assert caregiver != null;

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

    /**
     * Populates the combobox for selection of a caregiver.
     * The list of all caregivers is loaded from the database using the {@link CaregiverDAO#readAll()}.
     */
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

    /**
     * Gathers a list of all treatment types from the database using the {@link TreatmentTypeDAO#readAll()} method
     * and populates the autocompleting text field with them to provide a better user experience.
     */
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

    /**
     * Searches for the abbreviated name of a caregiver in the list of all caregivers.
     * Throws an exception if the caregiver is not found.
     *
     * @param formattedName The name of the caregiver as it is displayed in the combobox.
     * @return The caregiver object with the given name or null if no caregiver with the given name exists.
     */
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

    /**
     * Creates a new treatment in the database by calling the {@link TreatmentDAO#create(Treatment)} method.
     *
     * @param treatment The treatment to be created.
     */
    private void createTreatment(Treatment treatment) {
        TreatmentDAO dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        try {
            dao.create(treatment);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Handles the 'Abbruch' button.
     */
    @FXML
    public void handleCancel() {
        stage.close();
    }
}