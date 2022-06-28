package model;

import datastorage.DAOFactory;
import datastorage.TreatmentTypeDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.sql.SQLException;
import java.util.Objects;

/**
 * This class represents a treatment type.
 */
public class TreatmentType {

    private long id;

    /**
     * The description of the treatment type as {@link StringProperty} to properly display it in the table view.
     */
    private StringProperty description;

    /**
     * @param id The id of the treatment type.
     */
    public TreatmentType(long id) {
        TreatmentTypeDAO treatmentTypeDAO = DAOFactory.getDAOFactory().createTreatmentTypeDAO();

        this.id = id;
        try {
            this.description = new SimpleStringProperty(capitalize(treatmentTypeDAO.readDescriptionById(id)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * @param description The description of the treatment type.
     */
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

    /**
     * @param id The id of the treatment type.
     * @param description The description of the treatment type.
     */
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


    /**
     * @return The id of the treatment type.
     */
    public long getId() {
        return id;
    }

    /**
     * @param id The new id of the treatment type.
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * @return The descriptionProperty of the treatment type.
     */
    public StringProperty descriptionProperty() {
        return description;
    }

    /**
     * @return The description of the treatment type.
     */
    public String getDescription() {
        return description.get();
    }

    /**
     * @param word The word to capitalize.
     * @return The capitalized word.
     */
    private String capitalize(String word) {
        try {
            return word.substring(0, 1).toUpperCase() + word.substring(1);
        } catch (StringIndexOutOfBoundsException e) {
            return word;
        }
    }

    /**
     * @throws IllegalArgumentException If the description is empty.
     */
    private void throwExceptionWhenRequiredFieldIsEmpty() throws IllegalArgumentException {
        if (Objects.equals(getDescription().trim(), ""))
            throw new IllegalArgumentException("Die Beschreibung darf nicht leer sein.");
    }
}
