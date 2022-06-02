package model;

import datastorage.DAOFactory;
import datastorage.TreatmentTypeDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.SQLException;

public class TreatmentType {

    private long id;

    private StringProperty description;

    public TreatmentType(long id) {
        TreatmentTypeDAO treatmentTypeDAO = DAOFactory.getDAOFactory().createTreatmentTypeDAO();

        this.id = id;
        try {
            this.description = new SimpleStringProperty(treatmentTypeDAO.readDescriptionById(id));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TreatmentType(String description) {
        TreatmentTypeDAO treatmentTypeDAO = DAOFactory.getDAOFactory().createTreatmentTypeDAO();

        this.description = new SimpleStringProperty(description);

        try {
            this.id = treatmentTypeDAO.readIdByDescription(description);
            if (this.id == -1) {
                treatmentTypeDAO.create(this);
                this.setId(treatmentTypeDAO.readIdByDescription(description));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TreatmentType(long id, String description) {
        this.id = id;
        this.description = new SimpleStringProperty(description);
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }
}