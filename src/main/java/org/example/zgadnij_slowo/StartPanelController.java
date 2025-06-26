package org.example.zgadnij_slowo;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import java.util.ResourceBundle;

public class StartPanelController implements Initializable {

    @FXML private ComboBox<String> difficultyComboBox;
    @FXML private Button startGameButton;
    @FXML private Button rulesButton;
    @FXML private Button highScoresButton;
    @FXML private Button exitButton;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField nickField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        difficultyComboBox.setItems(FXCollections.observableArrayList("Łatwy", "Średni", "Trudny"));
        difficultyComboBox.setValue("Średni");
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Jedzenie", "Kolory", "Kraje", "Zawody", "Zwierzeta", "Wszystkie"
        ));
        categoryComboBox.setValue("Jedzenie");

        exitButton.setOnAction(event -> Platform.exit());
        rulesButton.setOnAction(event -> showRules());
        highScoresButton.setOnAction(event -> showHighScores());
        startGameButton.setOnAction(event -> {
            try {
                startGame(
                        String.valueOf(difficultyComboBox.getValue()),
                        String.valueOf(categoryComboBox.getValue())
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void startGame(String difficulty, String category) throws IOException {
        String fxmlToLoad;
        if (difficulty.equalsIgnoreCase("trudny")) {
            fxmlToLoad = "/zgadnijSlowo7.fxml";
        } else if (difficulty.equalsIgnoreCase("łatwy")) {
            fxmlToLoad = "/zgadnijSlowo5.fxml";
        } else {
            fxmlToLoad = "/zgadnijSlowo6.fxml";
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlToLoad));
        Parent gameRoot = loader.load();
        ZgadnijSlowoController ctrl = loader.getController();
        ctrl.init(category, difficulty);
        ctrl.setNick(nickField.getText());
        ctrl.setStartPanelController(this);
        Scene currentScene = startGameButton.getScene();
        Stage stage = (Stage) currentScene.getWindow();
        stage.setScene(new Scene(gameRoot));
        stage.show();
    }

    private void showRules() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Zasady gry");
        alert.setHeaderText("Jak grać w Zgadnij Słowo");
        alert.setContentText(
                "1. Twoim zadaniem jest odgadnięcie słowa w 6 próbach.\n\n" +
                        "2. Po każdej próbie kolor kafelków zmieni się, aby pokazać, jak blisko byłeś rozwiązania:\n" +
                        "   - Zielony: litera jest w słowie i we właściwym miejscu\n" +
                        "   - Żółty: litera jest w słowie, ale w złym miejscu\n" +
                        "   - Szary: litera nie występuje w słowie\n\n" +
                        "3. Poziomy trudności:\n" +
                        "   - Łatwy: słowa na 5 liter\n" +
                        "   - Średni: słowa na 6 liter\n" +
                        "   - Trudny: słowa na 7 liter\n\n" +
                        "Powodzenia!"
        );
        alert.showAndWait();
    }
    public void showHighScores() {
        try (BufferedReader reader = new BufferedReader(new FileReader("scores.txt"))) {
            StringBuilder scores = new StringBuilder("Najlepsze wyniki:\n");
            String line;
            while ((line = reader.readLine()) != null) {
                scores.append(line).append("\n");
            }
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Najlepsze wyniki");
            alert.setHeaderText(null);
            alert.setContentText(scores.toString());
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Błąd");
            alert.setHeaderText("Nie można odczytać pliku z wynikami");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
    public String nickOfPlayer(){
        return nickField.getText().trim();
    }
}
