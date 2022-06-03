package datastorage;

public class DAOFactory {

    private static DAOFactory instance;

    private DAOFactory() {

    }

    public static DAOFactory getDAOFactory() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    public TreatmentDAO createTreatmentDAO() {
        return new TreatmentDAO(ConnectionBuilder.getConnection());
    }

    public PatientDAO createPatientDAO() {
        return new PatientDAO(ConnectionBuilder.getConnection());
    }

    public TreatmentTypeDAO createTreatmentTypeDAO() {
        return new TreatmentTypeDAO(ConnectionBuilder.getConnection());
    }

    public PersonDAO createPersonDAO() {
        return new PersonDAO(ConnectionBuilder.getConnection());
    }
}