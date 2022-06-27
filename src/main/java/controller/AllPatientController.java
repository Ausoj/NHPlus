package controller;

import datastorage.DAOFactory;
import datastorage.PatientDAO;
import datastorage.PersonDAO;
import datastorage.TreatmentDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import model.Patient;
import utils.DateConverter;
import utils.DialogueManager;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;


/**
 * The <code>AllPatientController</code> contains the entire logic of the patient view. It determines which data is displayed and how to react to events.
 */
public class AllPatientController {
    @FXML
    private TableView<Patient> tableView;
    @FXML
    private TableColumn<Patient, Integer> colID;
    @FXML
    private TableColumn<Patient, String> colFirstName;
    @FXML
    private TableColumn<Patient, String> colSurname;
    @FXML
    private TableColumn<Patient, String> colDateOfBirth;
    @FXML
    private TableColumn<Patient, String> colCareLevel;
    @FXML
    private TableColumn<Patient, String> colRoom;

    @FXML
    Button btnDelete;
    @FXML
    Button btnAdd;
    @FXML
    TextField txtSurname;
    @FXML
    TextField txtFirstname;
    @FXML
    TextField txtBirthday;
    @FXML
    TextField txtCareLevel;
    @FXML
    TextField txtRoom;

    private final ObservableList<Patient> tableviewContent = FXCollections.observableArrayList();
    private PatientDAO dao;

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

        this.colDateOfBirth.setCellValueFactory(new PropertyValueFactory<>("dateOfBirth"));
        this.colDateOfBirth.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colCareLevel.setCellValueFactory(new PropertyValueFactory<>("careLevel"));
        this.colCareLevel.setCellFactory(TextFieldTableCell.forTableColumn());

        this.colRoom.setCellValueFactory(new PropertyValueFactory<>("roomNumber"));
        this.colRoom.setCellFactory(TextFieldTableCell.forTableColumn());

        //Anzeigen der Daten
        this.tableView.setItems(this.tableviewContent);
    }

    /**
     * handles new firstname value
     *
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditFirstname(TableColumn.CellEditEvent<Patient, String> event) {
        try {
            event.getRowValue().setFirstName(event.getNewValue());
            doUpdate(event);
        } catch (IllegalArgumentException e) {
            DialogueManager.getInstance().showAlert("Patient konnte nicht angepasst werden", e);
            readAllAndShowInTableView();
        }
    }

    /**
     * handles new surname value
     *
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditSurname(TableColumn.CellEditEvent<Patient, String> event) {
        try {
            event.getRowValue().setSurname(event.getNewValue());
            doUpdate(event);
        } catch (IllegalArgumentException e) {
            DialogueManager.getInstance().showAlert("Patient konnte nicht angepasst werden", e);
            readAllAndShowInTableView();
        }
    }

    /**
     * handles new birthdate value
     *
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditDateOfBirth(TableColumn.CellEditEvent<Patient, String> event) {
        try {
            event.getRowValue().setDateOfBirth(event.getNewValue());
            doUpdate(event);
        } catch (IllegalArgumentException e) {
            DialogueManager.getInstance().showAlert("Patient konnte nicht angepasst werden", e);
            readAllAndShowInTableView();
        }
    }

    /**
     * handles new careLevel value
     *
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditCareLevel(TableColumn.CellEditEvent<Patient, String> event) {
        try {
            event.getRowValue().setCareLevel(event.getNewValue());
            doUpdate(event);
        } catch (IllegalArgumentException e) {
            DialogueManager.getInstance().showAlert("Patient konnte nicht angepasst werden", e);
            readAllAndShowInTableView();
        }
    }

    /**
     * handles new roomNumber value
     *
     * @param event event including the value that a user entered into the cell
     */
    @FXML
    public void handleOnEditRoomNumber(TableColumn.CellEditEvent<Patient, String> event) {
        try {
            event.getRowValue().setRoomNumber(event.getNewValue());
            doUpdate(event);
        } catch (IllegalArgumentException e) {
            DialogueManager.getInstance().showAlert("Patient konnte nicht angepasst werden", e);
            readAllAndShowInTableView();
        }
    }


    /**
     * updates a patient by calling the update-Method in the {@link PatientDAO} & {@link PersonDAO}
     *
     * @param t row to be updated by the user (includes the patient)
     */
    private void doUpdate(TableColumn.CellEditEvent<Patient, String> t) {
        PersonDAO personDAO = DAOFactory.getDAOFactory().createPersonDAO();
        try {
            personDAO.update(t.getRowValue());
            dao.update(t.getRowValue());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * calls {@link PatientDAO#readAll()} and shows patients in the table
     */
    private void readAllAndShowInTableView() {
        this.tableviewContent.clear();
        this.dao = DAOFactory.getDAOFactory().createPatientDAO();
        List<Patient> allPatients;
        try {
            allPatients = dao.readAll();
            this.tableviewContent.addAll(allPatients);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * handles a delete-click-event.
     * Calls the {@link PatientDAO#deleteById(long)}, {@link TreatmentDAO#deleteByPid(long)} and {@link PersonDAO#deleteById(long)} methods,
     * and removes the selected Patient from the table.
     */
    @FXML
    public void handleDeleteRow() {
        TreatmentDAO tDao = DAOFactory.getDAOFactory().createTreatmentDAO();
        PersonDAO personDAO = DAOFactory.getDAOFactory().createPersonDAO();
        Patient selectedItem = this.tableView.getSelectionModel().getSelectedItem();
        try {
            tDao.deleteByPid(selectedItem.getId());
            dao.deleteById(selectedItem.getId());
            personDAO.deleteById(selectedItem.getPersonId());
            this.tableView.getItems().remove(selectedItem);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * handles an add-click-event. Creates a patient and calls {@link PatientDAO#create(Patient)}.
     * Also displays a dialogue if the patient could not be created.
     */
    @FXML
    public void handleAdd() {
        String surname = this.txtSurname.getText();
        String firstname = this.txtFirstname.getText();
        String birthday = this.txtBirthday.getText();
        String careLevel = this.txtCareLevel.getText();
        String room = this.txtRoom.getText();
        try {
            LocalDate date = DateConverter.convertStringToLocalDate(birthday);
            Patient p = new Patient(firstname, surname, date, careLevel, room);
            dao.create(p);
            clearTextFields();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            DialogueManager.getInstance().showAlert("Patient konnte nicht erstellt werden", e);
        }
        readAllAndShowInTableView();
    }

    /**
     * removes content from all text fields
     */
    private void clearTextFields() {
        this.txtFirstname.clear();
        this.txtSurname.clear();
        this.txtBirthday.clear();
        this.txtCareLevel.clear();
        this.txtRoom.clear();
    }
}