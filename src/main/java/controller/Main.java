package controller;

import datastorage.ConnectionBuilder;
import datastorage.DAOFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import utils.DSGVOCleaner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * Main class of the application.
 */
public class Main extends Application {

    public static Stage primaryStage;

    /**
     * @param primaryStage The primary stage of the application.
     */
    @Override
    public void start(Stage primaryStage) {
        Main.primaryStage = primaryStage;
        mainWindow();
    }

    /**
     * This method handles the creation of the main window of the application
     * It is also responsible for loading the LoginView, and the styling for the current scene.
     * After the creation of the main window scene, the {@link DSGVOCleaner#run()} methos is called.
     */
    public void mainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/LoginView.fxml"));
            GridPane pane = loader.load();

            Scene scene = new Scene(pane);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Application.css")).toExternalForm());
            primaryStage.setTitle("NHPlus");
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();

            DAOFactory daoFactory = DAOFactory.getDAOFactory();
            DSGVOCleaner cleaner = new DSGVOCleaner(daoFactory);
//          Run DSGVO Cleaner
            cleaner.run();

            primaryStage.setOnCloseRequest(e -> {
                ConnectionBuilder.closeConnection();
                Platform.exit();
                System.exit(0);
            });
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}