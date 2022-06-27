package model;

import datastorage.DAOFactory;
import datastorage.TreatmentTypeDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.SQLException;
import java.util.Objects;

public class TreatmentType {

    private long id;

    private StringProperty description;

    public TreatmentType(long id) {
        TreatmentTypeDAO treatmentTypeDAO = DAOFactory.getDAOFactory().createTreatmentTypeDAO();

        this.id = id;
        try {
            this.description = new SimpleStringProperty(capitalize(treatmentTypeDAO.readDescriptionById(id)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public TreatmentType(String description) {
        TreatmentTypeDAO treatmentTypeDAO = DAOFactory.getDAOFactory().createTreatmentTypeDAO();
        description = capitalize(description);
        this.description = new SimpleStringProperty(description);
        throwExceptionWhenRequiredFieldIsEmpty();

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
        TreatmentTypeDAO treatmentTypeDAO = DAOFactory.getDAOFactory().createTreatmentTypeDAO();
        description = capitalize(description);

        this.id = id;
        this.description = new SimpleStringProperty(description);
        throwExceptionWhenRequiredFieldIsEmpty();

//        Check if new description already exists
        try {
//        YES: Set id to this object
            this.id = treatmentTypeDAO.readIdByDescription(description);
        } catch (SQLException e) {
            e.printStackTrace();
        }
//        NO:
        if (this.id != id) {
//        Check if treatment type is used more than once and the id has not already been changed
            if (treatmentTypeDAO.isTreatmentTypeUsedMoreThanOnce(id)) {
//        YES: Create new treatment type with that description
                try {
                    treatmentTypeDAO.create(this);
                    this.setId(treatmentTypeDAO.readIdByDescription(description));
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
//        NO: Update treatment type description
                try {
                    treatmentTypeDAO.update(this);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
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

    private String capitalize(String word) {
        try {
            return word.substring(0, 1).toUpperCase() + word.substring(1);
        } catch (StringIndexOutOfBoundsException e) {
            return word;
        }
    }

    private void throwExceptionWhenRequiredFieldIsEmpty() throws IllegalArgumentException {
        if (Objects.equals(getDescription().trim(), ""))
            throw new IllegalArgumentException("Die Beschreibung darf nicht leer sein.");
    }
}
