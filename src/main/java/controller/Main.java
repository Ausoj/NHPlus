package controller;

import datastorage.ConnectionBuilder;
import datastorage.DAOFactory;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import utils.DSGVOCleaner;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class Main extends Application {

    public static Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        mainWindow();
    }

    public void mainWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/LoginView.fxml"));
            GridPane pane = loader.load();

            Scene scene = new Scene(pane);
            scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/Application.css")).toExternalForm());
            this.primaryStage.setTitle("NHPlus");
            this.primaryStage.setScene(scene);
            this.primaryStage.setResizable(false);
            this.primaryStage.show();

            DAOFactory daoFactory = DAOFactory.getDAOFactory();
            DSGVOCleaner cleaner = new DSGVOCleaner(daoFactory);
//          Run DSGVO Cleaner
            cleaner.run();

            this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent e) {
                    ConnectionBuilder.closeConnection();
                    Platform.exit();
                    System.exit(0);
                }
            });
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}