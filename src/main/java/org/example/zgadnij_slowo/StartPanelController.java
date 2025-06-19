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
import javafx.stage.Stage;


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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        difficultyComboBox.setItems(FXCollections.observableArrayList(
                "Łatwy", "Średni", "Trudny"
        ));
        difficultyComboBox.setValue("Średni");
        categoryComboBox.setItems(FXCollections.observableArrayList(
                "Jedzenie","Kolory","Kraje","Zawody","Zwierzeta","Wszystkie kategorie"
        ));
        categoryComboBox.setValue("Wszystkie kategorie");

        WordDatabase wordDatabase = new WordDatabase();
        categoryComboBox.setItems(FXCollections.observableArrayList(wordDatabase.getCategories()));
        categoryComboBox.setValue(wordDatabase.getCategories()[0]);


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
        String key;
        if (difficulty.equalsIgnoreCase("trudny")) {
            key = category.toLowerCase() + "7liter";
        } else {
            key = category.toLowerCase() + "6liter";
        }

        String fxmlToLoad;
        if (key.endsWith("6liter")) {
            fxmlToLoad = "/zgadnijSlowo6.fxml";
        } else if (key.endsWith("7liter")) {
            fxmlToLoad = "/zgadnijSlowo7.fxml";
        } else {
            fxmlToLoad = "/zgadnijSlowo6.fxml";
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlToLoad));
        Parent gameRoot = loader.load();

        // Pobierz controller i zainicjalizuj go poprawnie!
        ZgadnijSlowoController ctrl = loader.getController();
        ctrl.init(categoryComboBox.getValue().toLowerCase(), difficultyComboBox.getValue());


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
                "1. Twoim zadaniem jest odgadnięcie 5-literowego słowa w 6 próbach.\n\n" +
                        "2. Po każdej próbie kolor kafelków zmieni się, aby pokazać, jak blisko byłeś rozwiązania:\n" +
                        "   - Zielony: litera jest w słowie i we właściwym miejscu\n" +
                        "   - Żółty: litera jest w słowie, ale w złym miejscu\n" +
                        "   - Szary: litera nie występuje w słowie\n\n" +
                        "3. Poziomy trudności:\n" +
                        "   - Łatwy: podpowiedzi dla wszystkich liter\n" +
                        "   - Średni: standardowe zasady\n" +
                        "   - Trudny: odkryte litery muszą być użyte w kolejnych próbach\n\n" +
                        "Powodzenia!"
        );
        alert.showAndWait();
    }

    private void showHighScores() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Tabela wyników");
        alert.setHeaderText("Najlepsze wyniki");
        alert.setContentText(
                "1. Gracz123 - 10 zwycięstw - Średni czas: 2:15\n" +
                        "2. MistrzWordle - 8 zwycięstw - Średni czas: 2:30\n" +
                        "3. SuperGracz - 7 zwycięstw - Średni czas: 2:45\n" +
                        "4. WordleFan - 5 zwycięstw - Średni czas: 3:10\n" +
                        "5. NowyGracz - 3 zwycięstwa - Średni czas: 3:45"
        );
        alert.showAndWait();
    }

    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
