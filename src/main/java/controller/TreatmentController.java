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
    private Button btnChange;
    @FXML
    private Button btnCancel;
    @FXML
    private ComboBox<String> comboCaregiver;

    private AllTreatmentController controller;
    private Stage stage;
    private Patient patient;
    private Treatment treatment;
    private List<Caregiver> allCaregivers;

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

    // TODO: Create CaregiverCollection IF it would be beneficial elsewhere (in another file)
    private Caregiver searchInList(String formattedName) {
        for (Caregiver caregiver : allCaregivers) {
            if (formattedName.equals(caregiver.getAbbreviatedName())) {
                return caregiver;
            }
        }
        return null;
    }


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

    @FXML
    public void handleCancel() {
        stage.close();
    }
}