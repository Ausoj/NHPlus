package controller;

import datastorage.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import model.Caregiver;
import utils.DateConverter;
import utils.DialogueManager;

import java.sql.SQLException;
import java.util.List;
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


    private final ObservableList<Caregiver> tableviewContent = FXCollections.observableArrayList();
    private CaregiverDAO dao;

    /**
     * Initializes the corresponding fields. Is called as soon as the corresponding FXML file is to be displayed.
     */
    public void initialize() {
        readAllAndShowInTableView();

        this.colID.setCellValueFactory(new PropertyValueFactory<>("id"));

        //CellValueFactory zum Anzeigen der Daten in der TableView
        this.colFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        //CellFactory zum Schreiben innerhalb der Tabelle
        this.colFirstName.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colSurname.setCellValueFactory(new PropertyValueFactory<>("surname"));
        this.colSurname.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colPhoneNumber.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
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
            DialogueManager.getInstance().showAlert("Pfleger konnte nicht angepasst werden", e);
            readAllAndShowInTableView();
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
            DialogueManager.getInstance().showAlert("Pfleger konnte nicht angepasst werden", e);
            readAllAndShowInTableView();
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
            DialogueManager.getInstance().showAlert("Pfleger konnte nicht angepasst werden", e);
            readAllAndShowInTableView();
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
            this.tableviewContent.addAll(allCaregivers);
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

//      When Caregiver has no treatments (10 years) else lock
        try {
            if (DateConverter.isWithinLast10Years(dao.getLastTreatmentTime(selectedItem.getId()))) {
                lockCaregiver(selectedItem);
            } else {
                deleteCaregiver(selectedItem);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void lockCaregiver(Caregiver selectedItem) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pfleger/in sperren");
        alert.setHeaderText("Der ausgewählte Pfleger hat in den letzten 10 Jahren Behandlungen durchgeführt.\n" +
                "Möchten Sie den Pfleger/in sperren?");
        alert.setContentText(selectedItem.getAbbreviatedName() + " wird gesperrt.");
        Optional<ButtonType> choice = alert.showAndWait();
        if (!choice.get().getText().equals("OK")) return;

        dao.lockCaregiver(selectedItem);
        this.tableView.getItems().remove(selectedItem);

    }

    private void deleteCaregiver(Caregiver selectedItem) throws SQLException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Pfleger/in löschen");
        alert.setHeaderText("Möchten Sie den Pfleger/in wirklich löschen?");
        alert.setContentText(selectedItem.getAbbreviatedName() + " wird endgültig gelöscht.");
        Optional<ButtonType> choice = alert.showAndWait();
        if (!choice.get().getText().equals("OK")) return;

        dao.deleteCaregiver(selectedItem);
        this.tableView.getItems().remove(selectedItem);
    }

    /**
     * handles an add-click-event. Creates a patient and calls the create method in the {@link PatientDAO}
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
            clearTextFields();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            DialogueManager.getInstance().showAlert("Pfleger konnte nicht erstellt werden", e);
        }
    }

    /**
     * removes content from all text fields
     */
    private void clearTextFields() {
        this.txtFirstname.clear();
        this.txtSurname.clear();
        this.txtPhoneNumber.clear();

    }
}
