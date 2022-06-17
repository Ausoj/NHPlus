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

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class TreatmentController {
    @FXML
    private Label lblPatientName;
    @FXML
    private Label lblCarelevel;
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

    // Todo: Add Caregiver selection

    public void initializeController(AllTreatmentController controller, Stage stage, Treatment treatment) {
        this.stage = stage;
        this.controller = controller;
        PatientDAO pDao = DAOFactory.getDAOFactory().createPatientDAO();
        try {
            this.patient = pDao.read((int) treatment.getPid());
            this.treatment = treatment;
            showData();
            populateDescriptionTextField();
            populateCaregiverCombobox();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showData() {
        this.lblPatientName.setText(patient.getSurname() + ", " + patient.getFirstName());
        this.lblCarelevel.setText(patient.getCareLevel());
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
        this.treatment.setCid(caregiver.getCid());
        this.treatment.setDate(this.datepicker.getValue().toString());
        this.treatment.setBegin(txtBegin.getText());
        this.treatment.setEnd(txtEnd.getText());
        this.treatment.setType(new TreatmentType(this.treatment.getType().getId(), txtDescription.getText()));
        this.treatment.setRemarks(taRemarks.getText());
        doUpdate();
        controller.readAllAndShowInTableView();
        stage.close();
    }

    public void populateCaregiverCombobox() {
        CaregiverDAO caregiverDAO = DAOFactory.getDAOFactory().createCaregiverDAO();
        ObservableList<String> allCaregiversNames = FXCollections.observableArrayList();
        try {
            allCaregivers = caregiverDAO.readAll();
            for (Caregiver caregiver : allCaregivers) {
                allCaregiversNames.add(abbreviateCaregiverName(caregiver));
            }
            comboCaregiver.setItems(allCaregiversNames);
            comboCaregiver.getSelectionModel().select(abbreviateCaregiverName(caregiverDAO.read(this.treatment.getCid())));
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

// TODO: Put in Caregiver
    private String abbreviateCaregiverName(Caregiver caregiver) {
        String firstName = caregiver.getFirstName().substring(0, 1);
        return String.format("%s. %s", firstName, caregiver.getSurname());
    }

// TODO: Maybe create CaregiverCollection
    private Caregiver searchInList(String formattedName) {
        for (Caregiver caregiver : allCaregivers) {
            if (formattedName.equals(abbreviateCaregiverName(caregiver))) {
                return caregiver;
            }
        }
        return null;
    }


    private void doUpdate() {
        TreatmentDAO dao = DAOFactory.getDAOFactory().createTreatmentDAO();
        TreatmentTypeDAO treatmentTypeDAO = DAOFactory.getDAOFactory().createTreatmentTypeDAO();
        try {
            dao.update(treatment);
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