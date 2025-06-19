package org.example.zgadnij_slowo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.net.URL;
import java.util.*;

public class ZgadnijSlowoController implements Initializable {

    @FXML private GridPane gameGrid;
    @FXML private VBox keyboardBox;
    @FXML private Button gameModeButton;
    @FXML private Button rulesButton;
    @FXML private Button highScoresButton;
    @FXML private Button backButton;
    @FXML private Label timerLabel;
    @FXML private Button hintButton;

    private int wordLength = 6;      // domyślnie
    private int maxTries = 6;
    private int currentRow = 0;
    private int currentCol = 0;
    private String targetWord;
    private String category = "Jedzenie";
    private String difficulty = "Średni";
    private List<List<Label>> gridLabels = new ArrayList<>();
    private WordDatabase wordDatabase;
    private StringBuilder currentGuess = new StringBuilder();
    private boolean gameOver = false;

    // WYWOŁAJ TĄ METODĘ ze StartPanelu!
    public void init(String category, String difficulty) {
        this.category = category.toLowerCase();
        this.difficulty = difficulty;
        this.wordDatabase = new WordDatabase();
        if (difficulty.equalsIgnoreCase("trudny")) {
            this.wordLength = 7;
        } else {
            this.wordLength = 6;
        }
        setupGame();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupListeners();
    }

    private void setupGame() {
        if (wordDatabase == null) {
            wordDatabase = new WordDatabase();
        }
        wordDatabase.setCategoryAndDifficulty(category, difficulty);
        this.targetWord = wordDatabase.getRandomWord().toUpperCase();
        this.currentRow = 0;
        this.currentCol = 0;
        this.currentGuess = new StringBuilder();
        this.gameOver = false;

        setupGrid();
        setupKeyboard();
        System.out.println("Wylosowane słowo: " + targetWord); // DEBUG
    }




    private void setupGrid() {
        gameGrid.getChildren().clear();
        gridLabels.clear();

        for (int row = 0; row < maxTries; row++) {
            List<Label> rowLabels = new ArrayList<>();
            for (int col = 0; col < wordLength; col++) {
                Label label = new Label("");
                label.setMinSize(50, 50);
                label.setAlignment(Pos.CENTER);
                label.setStyle("-fx-background-color: white; -fx-border-color: #d3d3d3; -fx-border-width: 2; -fx-font-size: 24px; -fx-font-weight: bold;");
                gameGrid.add(label, col, row);
                rowLabels.add(label);
            }
            gridLabels.add(rowLabels);
        }
    }

    private void setupKeyboard() {
        // Możesz dodać styl wyłączenia dla klawiszy, ale tu wystarczy blokada w handleKeyboardInput/gameOver
        for (Node rowBox : keyboardBox.getChildren()) {
            if (rowBox instanceof HBox) {
                for (Node btnNode : ((HBox) rowBox).getChildren()) {
                    if (btnNode instanceof Button) {
                        Button btn = (Button) btnNode;
                        btn.setFocusTraversable(false);
                        btn.setOnAction(e -> handleKeyboardInput(btn));
                    }
                }
            }
        }
    }

    private void handleKeyboardInput(Button btn) {
        if (gameOver) return;

        String key = btn.getText();

        if ("Enter".equals(key)) {
            if (currentGuess.length() == wordLength) {
                submitGuess();
            }
        } else if ("⌫".equals(key)) {
            if (currentCol > 0 && currentGuess.length() > 0) {
                removeLetter();
            }
        } else if (key.length() == 1 && currentCol < wordLength && currentRow < maxTries) {
            addLetter(key.toUpperCase());
        }
    }

    private void addLetter(String letter) {
        if (gameOver) return;
        if (currentCol < wordLength) {
            gridLabels.get(currentRow).get(currentCol).setText(letter);
            currentGuess.append(letter);
            currentCol++;
        }
    }

    private void removeLetter() {
        if (gameOver) return;
        if (currentCol > 0 && currentGuess.length() > 0) {
            currentCol--;
            gridLabels.get(currentRow).get(currentCol).setText("");
            currentGuess.deleteCharAt(currentGuess.length() - 1);
        }
    }

    private void submitGuess() {
        if (gameOver) return;
        String guess = currentGuess.toString();

        if (guess.length() != wordLength) {
            showAlert("Błąd", "Słowo musi mieć dokładnie " + wordLength + " liter.");
            return;
        }

        colorGuessRow(guess);

        if (guess.equalsIgnoreCase(targetWord)) {
            showAlert("Brawo!", "Odgadłeś słowo: " + targetWord);
            gameOver = true;
        } else {
            currentRow++;
            currentCol = 0;
            currentGuess = new StringBuilder();

            if (currentRow >= maxTries) {
                showAlert("Przegrana", "Nie udało się! Szukane słowo to: " + targetWord);
                gameOver = true;
            }
        }
    }

    // Wordle: zielony = trafione miejsce, żółty = litera, ale nie tu, szary = brak litery
    private void colorGuessRow(String guess) {
        guess = guess.toUpperCase();
        String answer = targetWord.toUpperCase();

        // Przygotuj statusy do logiki wordle
        boolean[] answerUsed = new boolean[wordLength];
        boolean[] guessGreen = new boolean[wordLength];

        // 1. Zielone: idealne trafienia
        for (int i = 0; i < wordLength; i++) {
            Label cell = gridLabels.get(currentRow).get(i);
            char g = guess.charAt(i);
            char a = answer.charAt(i);

            if (g == a) {
                cell.setStyle("-fx-background-color: #6aaa64; -fx-border-color: #d3d3d3; -fx-border-width: 2; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
                answerUsed[i] = true;
                guessGreen[i] = true;
            } else {
                cell.setStyle("-fx-background-color: #787c7e; -fx-border-color: #d3d3d3; -fx-border-width: 2; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
            }
        }
        // 2. Żółte: litera jest gdzieś indziej (i nie została już przypisana zielonemu)
        for (int i = 0; i < wordLength; i++) {
            if (!guessGreen[i]) {
                char g = guess.charAt(i);
                for (int j = 0; j < wordLength; j++) {
                    if (!answerUsed[j] && g == answer.charAt(j)) {
                        Label cell = gridLabels.get(currentRow).get(i);
                        cell.setStyle("-fx-background-color: #c9b458; -fx-border-color: #d3d3d3; -fx-border-width: 2; -fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: white;");
                        answerUsed[j] = true;
                        break;
                    }
                }
            }
        }
    }

    private void setupListeners() {
        gameModeButton.setOnAction(e -> showInfo("Zmiana trybu gry", "Zmień tryb w menu głównym"));
        rulesButton.setOnAction(e -> showInfo("Zasady gry", "1. Odgadnij słowo\n2. Masz 6 prób\n3. Kolory podpowiedzi jak w Wordle"));
        highScoresButton.setOnAction(e -> showInfo("Najlepsze wyniki", "Wyniki znajdziesz w menu głównym"));
        backButton.setOnAction(e -> Platform.exit());
        hintButton.setOnAction(e -> {
            if (targetWord == null || targetWord.isEmpty()) {
                showInfo("Podpowiedź", "Słowo jeszcze nie zostało wylosowane.");
            } else {
                showInfo("Podpowiedź", "Pierwsza litera: " + targetWord.charAt(0));
            }
        });
    }

    private void showAlert(String title, String text) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(text);
            alert.showAndWait();
        });
    }

    private void showInfo(String title, String text) {
        showAlert(title, text);
    }
}
