package controller;

import datastorage.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import model.Caregiver;
import model.Treatment;

import java.sql.SQLException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class AllCaregiverController {

    @FXML
    private TableView<Caregiver> tableView;
    @FXML
    private TableColumn<Caregiver, Integer> colID;
    @FXML
    private TableColumn<Caregiver, String> colFirstName;
    @FXML
    private TableColumn<Caregiver, String> colSurname;
    @FXML
    private TableColumn<Caregiver, String> colPhoneNumber;


    @FXML
    Button btnDelete;
    @FXML
    Button btnAdd;
    @FXML
    TextField txtSurname;
    @FXML
    TextField txtFirstname;
    @FXML
    TextField txtPhoneNumber;


    private ObservableList<Caregiver> tableviewContent = FXCollections.observableArrayList();
    private CaregiverDAO dao;

    /**
     * Initializes the corresponding fields. Is called as soon as the corresponding FXML file is to be displayed.
     */
    public void initialize() {
        readAllAndShowInTableView();

        this.colID.setCellValueFactory(new PropertyValueFactory<Caregiver, Integer>("id"));

        //CellValuefactory zum Anzeigen der Daten in der TableView
        this.colFirstName.setCellValueFactory(new PropertyValueFactory<Caregiver, String>("firstName"));
        //CellFactory zum Schreiben innerhalb der Tabelle
        this.colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colSurname.setCellValueFactory(new PropertyValueFactory<Caregiver, String>("surname"));
        this.colSurname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colPhoneNumber.setCellValueFactory(new PropertyValueFactory<Caregiver, String>("phoneNumber"));
        this.colPhoneNumber.setCellFactory(TextFieldTableCell.forTableColumn());


        //Anzeigen der Daten
        this.tableView.setItems(this.tableviewContent);
    }

    /**
     * handles new firstname value
     *
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditFirstname(TableColumn.CellEditEvent<Caregiver, String> event) {
        try {
            event.getRowValue().setFirstName(event.getNewValue());
            doUpdate(event);
        } catch (IllegalArgumentException e) {
            showAlert("Pfleger konnte nicht angepasst werden", e);
        }
    }

    /**
     * handles new surname value
     *
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Caregiver, String> event) {
        try {
            event.getRowValue().setSurname(event.getNewValue());
            doUpdate(event);
        } catch (IllegalArgumentException e) {
            showAlert("Pfleger konnte nicht angepasst werden", e);
        }
    }

    /**
     * handles new birthdate value
     *
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditPhoneNumber(TableColumn.CellEditEvent<Caregiver, String> event) {
        try {
            event.getRowValue().setPhoneNumber(event.getNewValue());
            doUpdate(event);
        } catch (IllegalArgumentException e) {
            showAlert("Pfleger konnte nicht angepasst werden", e);
        }
    }


    /**
     * updates a patient by calling the update-Method in the {@link PatientDAO}
     *
     * @param t row to be updated by the user (includes the patient)
     */
    private void doUpdate(TableColumn.CellEditEvent<Caregiver, String> t) {
        PersonDAO personDAO = DAOFactory.getDAOFactory().createPersonDAO();
        try {
            personDAO.update(t.getRowValue());
            dao.update(t.getRowValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * calls readAll in {@link PatientDAO} and shows patients in the table
     */
    private void readAllAndShowInTableView() {
        this.tableviewContent.clear();
        this.dao = DAOFactory.getDAOFactory().createCaregiverDAO();
        List<Caregiver> allCaregivers;
        try {
            allCaregivers = dao.readAll();
            for (Caregiver c : allCaregivers) {
                this.tableviewContent.add(c);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * handles a delete-click-event. Calls the delete methods in the {@link PatientDAO} and {@link TreatmentDAO}
     */
    @FXML
    public void handleDeleteRow() {
        Caregiver selectedItem = this.tableView.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pfleger/in löschen");
        alert.setHeaderText("Möchten Sie den Pfleger/in wirklich löschen?");
        alert.setContentText(selectedItem.getAbbreviatedName() + " wird endgültig gelöscht.");
        Optional<ButtonType> choice = alert.showAndWait();
        if (!choice.get().getText().equals("OK")) return;

        PersonDAO personDAO = DAOFactory.getDAOFactory().createPersonDAO();
        TreatmentDAO treatmentDAO = DAOFactory.getDAOFactory().createTreatmentDAO();
        try {
            long caregiverId = selectedItem.getId();
            List<Treatment> treatments = treatmentDAO.readTreatmentsByCid(caregiverId);
            setCaregiverIdOnTreatmentsToDeleted(treatments);
            dao.deleteById(caregiverId);
            personDAO.deleteById(selectedItem.getPersonId());
            this.tableView.getItems().remove(selectedItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCaregiverIdOnTreatmentsToLocked(List<Treatment> treatments) throws SQLException {
        TreatmentDAO treatmentDAO = DAOFactory.getDAOFactory().createTreatmentDAO();
        for (Treatment treatment : treatments) {
            treatment.setCaregiverId(CaregiverDAO.LOCKED_ID);
            treatmentDAO.update(treatment);
        }
    }

    private void setCaregiverIdOnTreatmentsToDeleted(List<Treatment> treatments) throws SQLException {
        TreatmentDAO treatmentDAO = DAOFactory.getDAOFactory().createTreatmentDAO();
        for (Treatment treatment : treatments) {
            treatment.setCaregiverId(CaregiverDAO.DELETED_ID);
            treatmentDAO.update(treatment);
        }
    }

    /**
     * handles a add-click-event. Creates a patient and calls the create method in the {@link PatientDAO}
     */
    @FXML
    public void handleAdd() {
        String surname = this.txtSurname.getText();
        String firstname = this.txtFirstname.getText();
        String phoneNumber = this.txtPhoneNumber.getText();
        try {
            Caregiver c = new Caregiver(firstname, surname, phoneNumber);
            dao.create(c);
            readAllAndShowInTableView();
            clearTextfields();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            showAlert("Pfleger konnte nicht erstellt werden", e);
        }
    }

    private void showAlert(String heading, Exception e) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Achtung");
        alert.setHeaderText(heading);
        alert.setContentText(e.getMessage());
        alert.showAndWait();
        readAllAndShowInTableView();
    }

    /**
     * removes content from all textfields
     */
    private void clearTextfields() {
        this.txtFirstname.clear();
        this.txtSurname.clear();
        this.txtPhoneNumber.clear();

    }
}
