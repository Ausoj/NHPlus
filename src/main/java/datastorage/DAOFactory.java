package datastorage;

/**
 * This class is a singleton that provides access to the DAO objects.
 */
public class DAOFactory {

    private static DAOFactory instance;

    private DAOFactory() {

    }

    /**
     * @return the instance of the DAOFactory
     */
    public static DAOFactory getDAOFactory() {
        if (instance == null) {
            instance = new DAOFactory();
        }
        return instance;
    }

    /**
     * @return the {@link TreatmentDAO} object
     */
    public TreatmentDAO createTreatmentDAO() {
        return new TreatmentDAO(ConnectionBuilder.getConnection());
    }

    /**
     * @return the {@link PatientDAO} object
     */
    public PatientDAO createPatientDAO() {
        return new PatientDAO(ConnectionBuilder.getConnection());
    }


    /**
     * @return the {@link TreatmentTypeDAO} object
     */
    public TreatmentTypeDAO createTreatmentTypeDAO() {
        return new TreatmentTypeDAO(ConnectionBuilder.getConnection());
    }

    /**
     * @return {@link PersonDAO} object
     */
    public PersonDAO createPersonDAO() {
        return new PersonDAO(ConnectionBuilder.getConnection());
    }

    /**
     * @return the {@link CaregiverDAO} object
     */
    public CaregiverDAO createCaregiverDAO() {
        return new CaregiverDAO(ConnectionBuilder.getConnection());
    }

    /**
     * @return the {@link UserDAO} object
     */
    public UserDAO createUserDAO() {
        return new UserDAO(ConnectionBuilder.getConnection());
    }
}

