package org.example.zgadnij_slowo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ZgadnijSlowo extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/startPanel.fxml"));
        Parent root = loader.load();

        Scene scene = new Scene(root, 600, 500);

        primaryStage.setTitle("Zgadnij Slowo");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
