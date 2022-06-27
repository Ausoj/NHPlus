package controller;

import datastorage.*;
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
import java.util.ArrayList;
import java.util.List;

/**
 * The <code>TreatmentController</code> is the controller for the TreatmentView.
 * It handles the editing of a treatment.
 */
public class TreatmentController {
    @FXML
    private Label lblPatientName;
    @FXML
    private Label lblCareLevel;
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
    private Stage stage;
    private Patient patient;
    private Treatment treatment;
    private List<Caregiver> allCaregivers;

    /**
     * Initializes the controller class. This method is automatically called after the fxml file has been loaded.
     *
     * @param controller The controller of the all treatments view.
     * @param stage      The previous stage.
     * @param treatment  The treatment to edit.
     */
    public void initializeController(AllTreatmentController controller, Stage stage, Treatment treatment) {
        this.stage = stage;
        this.controller = controller;
        PatientDAO pDao = DAOFactory.getDAOFactory().createPatientDAO();
        try {
            this.patient = pDao.read((int) treatment.getPatientId());
            this.treatment = treatment;
            showData();
            populateDescriptionTextField();
            populateCaregiverCombobox();
//        Todo: prefill Date field with current date
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Shows the data of the treatment in the text fields.
     */
    private void showData() {
        this.lblPatientName.setText(patient.getSurname() + ", " + patient.getFirstName());
        this.lblCareLevel.setText(patient.getCareLevel());
        LocalDate date = DateConverter.convertStringToLocalDate(treatment.getDate());
        this.datepicker.setValue(date);
        this.txtBegin.setText(this.treatment.getBegin());
        this.txtEnd.setText(this.treatment.getEnd());
        this.txtDescription.setText(this.treatment.getType().getDescription());
        this.taRemarks.setText(this.treatment.getRemarks());
    }

    /**
     * Handles the saving of the treatment.
     * Sets the values of the current treatment to the values of the text fields and calls the {@link #doUpdate()} method.
     * Refreshes the table of all treatments afterwards using {@link AllTreatmentController#readAllAndShowInTableView()}.
     * Shows an alert if the treatment could not be saved.
     */
    @FXML
    public void handleChange() {
        Caregiver caregiver = searchInList(comboCaregiver.getSelectionModel().getSelectedItem());
//        Todo: Show dialogue box if caregiver is null
        assert caregiver != null;
        this.treatment.setCaregiverId(caregiver.getId());
        try {
            this.treatment.setDate(this.datepicker.getValue().toString());
            this.treatment.setBegin(txtBegin.getText());
            this.treatment.setEnd(txtEnd.getText());
            this.treatment.setType(new TreatmentType(this.treatment.getType().getId(), txtDescription.getText()));
            this.treatment.setRemarks(taRemarks.getText());
            doUpdate();
            controller.readAllAndShowInTableView();
            stage.close();
        } catch (IllegalArgumentException e) {
            DialogueManager.getInstance().showAlert("Behandlung konnte nicht gespeichert werden", e);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Populates the caregiver combobox with the abbreviated names of all caregivers.
     * Also preselects the caregiver of the currently edited treatment.
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
            Caregiver preselectedCaregiver = caregiverDAO.read(this.treatment.getCaregiverId());
            comboCaregiver.getSelectionModel().select(preselectedCaregiver.getAbbreviatedName());
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
        List<String> treatmentTypes = new ArrayList<>();
        try {
            List<TreatmentType> allTreatmentTypes = dao.readAll();
            for (TreatmentType type : allTreatmentTypes) {
                treatmentTypes.add(type.getDescription());
            }
            TextFields.bindAutoCompletion(txtDescription, treatmentTypes);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Searches for a caregiver in the list of all caregivers.
     *
     * @param formattedName The abbreviated name of the caregiver to search for.
     * @return The caregiver with the given abbreviated name or null.
     */
    // TODO: Create CaregiverCollection IF it would be beneficial elsewhere (in another file)
    private Caregiver searchInList(String formattedName) {
        for (Caregiver caregiver : allCaregivers) {
            if (formattedName.equals(caregiver.getAbbreviatedName())) {
                return caregiver;
            }
        }
        return null;
    }


    /**
     * Updates the treatment in the database using the {@link TreatmentDAO#update(Treatment)} method
     * while also updating the Caregivers last treatment date using {@link CaregiverDAO#setLastTreatment(Caregiver)}
     * as well as deleting unused {@link TreatmentType}s.
     */
    private void doUpdate() {
        TreatmentDAO dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        TreatmentTypeDAO treatmentTypeDAO = DAOFactory.getDAOFactory().createTreatmentTypeDAO();
        CaregiverDAO caregiverDAO = DAOFactory.getDAOFactory().createCaregiverDAO();
        try {
            dao.update(treatment);
            caregiverDAO.setLastTreatment(caregiverDAO.read(treatment.getCaregiverId()));
            treatmentTypeDAO.deleteUnusedTypes();
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