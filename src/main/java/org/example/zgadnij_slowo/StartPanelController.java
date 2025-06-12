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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        difficultyComboBox.setItems(FXCollections.observableArrayList(
                "Łatwy", "Średni", "Trudny"
        ));
        difficultyComboBox.setValue("Średni");

        startGameButton.setOnAction(event -> {
            try {
                String selectedDifficulty = difficultyComboBox.getValue();
                startGame(selectedDifficulty);
            } catch (Exception e) {
                showErrorAlert("Nie można uruchomić gry", e.getMessage());
                e.printStackTrace();
            }
        });
        rulesButton.setOnAction(event -> {
            showRules();
        });
        highScoresButton.setOnAction(event -> {
            showHighScores();
        });
        exitButton.setOnAction(event -> {
            Platform.exit();
        });
    }

    private void startGame(String difficulty) throws IOException {
        System.out.println("Rozpoczynanie gry z poziomem trudności: " + difficulty);

        // Ładowanie głównego ekranu gry
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/resources/startPanel.fxml"));
        Parent gameRoot = loader.load();

        // Przekazanie wybranego poziomu trudności do kontrolera gry
        ZgadnijSlowoController gameController = loader.getController();
        gameController.setDifficulty(difficulty);

        // Wyświetlenie ekranu gry
        Scene gameScene = new Scene(gameRoot);
        Stage stage = (Stage) startGameButton.getScene().getWindow();
        stage.setScene(gameScene);
        stage.setTitle("Zgadnij slowo - " + difficulty);
        stage.show();
    }

    private void showRules() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Zasady gry");
        alert.setHeaderText("Jak grać w Wordle");
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
        // przykladowe dane
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
