package utils;

import datastorage.CaregiverDAO;
import datastorage.DAOFactory;
import datastorage.PatientDAO;
import datastorage.TreatmentDAO;
import model.Caregiver;
import model.Treatment;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DSGVOCleaner {

    private final DAOFactory daoFactory;

    public DSGVOCleaner(DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    public void run() throws SQLException {
        System.out.println("Cleaner started");
        cleanUpPatients();
        cleanUpTreatments();
        cleanUpCaregivers();
    }

    private void cleanUpPatients() {
        System.out.println("Cleaning up patients");

        PatientDAO dao = daoFactory.createPatientDAO();
        lockPatients(dao);
        deletePatients(dao);
    }

    private void lockPatients(PatientDAO dao) {
//        3 months no treatment -> lock
    }

    private void deletePatients(PatientDAO dao) {
//        10 years no treatment -> delete

    }

    private void cleanUpTreatments() throws SQLException {
        System.out.println("Cleaning up treatments");

        TreatmentDAO dao = daoFactory.createTreatmentDAO();
        lockTreatments(dao);
        deleteTreatments(dao);
    }

    private void lockTreatments(TreatmentDAO dao) throws SQLException {
//        1 month no change -> lock
        long time1MonthAgo = DateConverter.getUnixMilliHowLongAgo("1 month");
        ResultSet result = dao.getAllTreatmentsWithoutChangeSince(time1MonthAgo);
        ArrayList<Treatment> treatments = new ArrayList<>();
        while (result.next()) {
            treatments.add(dao.read(result.getLong(1)));
        }

        for (Treatment treatment :
                treatments) {
            dao.lockTreatment(treatment);
            System.out.println("Locked treatment: ");
            System.out.println(treatment);
        }
    }

    private void deleteTreatments(TreatmentDAO dao) throws SQLException {
//        10 years no change -> delete
        long time10YearsAgo = DateConverter.getUnixMilliHowLongAgo("10 years");
        ResultSet result = dao.getAllTreatmentsWithoutChangeSince(time10YearsAgo);
        ArrayList<Treatment> treatments = new ArrayList<>();
        while (result.next()) {
            treatments.add(dao.read(result.getLong(1)));
        }
        for (Treatment treatment :
                treatments) {
            dao.deleteTreatment(treatment);
            System.out.println("Deleted treatment: ");
            System.out.println(treatment);
        }
    }

    private void cleanUpCaregivers() throws SQLException {
        System.out.println("Cleaning up caregivers");

        CaregiverDAO dao = daoFactory.createCaregiverDAO();
        lockCaregivers(dao);
        deleteCaregivers(dao);
    }

    private void lockCaregivers(CaregiverDAO dao) throws SQLException {
//        3 months no treatment -> lock
        long time3MonthsAgo = DateConverter.getUnixMilliHowLongAgo("3 months");
        ResultSet result = dao.getAllCaregiverIdsWithoutATreatmentSince(time3MonthsAgo);
        ArrayList<Caregiver> caregivers = new ArrayList<>();
        while (result.next()) {
            caregivers.add(dao.read(result.getLong(1)));
        }
        for (Caregiver caregiver :
                caregivers) {
            dao.lockCaregiver(caregiver);
            System.out.println("Locked caregiver: ");
            System.out.println(caregiver);
        }

    }

    private void deleteCaregivers(CaregiverDAO dao) throws SQLException {
//        10 years no treatment  -> delete
        long time10YearsAgo = DateConverter.getUnixMilliHowLongAgo("10 years");
        ResultSet result = dao.getAllCaregiverIdsWithoutATreatmentSince(time10YearsAgo);
        ArrayList<Caregiver> caregivers = new ArrayList<>();
        while (result.next()) {
            caregivers.add(dao.read(result.getLong(1)));
        }
        for (Caregiver caregiver :
                caregivers) {
            dao.deleteCaregiver(caregiver);
            System.out.println("Deleted caregiver: ");
            System.out.println(caregiver);
        }
    }
}
