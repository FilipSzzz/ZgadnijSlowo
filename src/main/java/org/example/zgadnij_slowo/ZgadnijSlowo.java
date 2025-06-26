package org.example.zgadnij_slowo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ZgadnijSlowo extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/startPanel.fxml"));
        Parent root = loader.load();
        primaryStage.setScene(new Scene(root, 702.0, 658.0));
        primaryStage.setTitle("Zgadnij Słowo");
        primaryStage.setResizable(false);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
