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
    @FXML private Button hintButton;
    @FXML private Label timerLabel;
    private Time time;
    @FXML private Pane rootPane;
    private StartPanelController startPanelController;


    private int wordLength = 6;
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
    private PlayerScore playerScore = new PlayerScore();
    private String playerName = "Gracz";

    public void init(String category, String difficulty) {
        this.category = category;
        this.difficulty = difficulty;
        this.wordDatabase = new WordDatabase();
        if (difficulty.equalsIgnoreCase("trudny")) {
            this.wordLength = 7;
        } else if (difficulty.equalsIgnoreCase("łatwy")) {
            this.wordLength = 5;
        }else{
            this.wordLength = 6;
        }
        setupGame();
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
        System.out.println("Wylosowane słowo: " + targetWord);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupListeners();
        setupGame();
        Platform.runLater(() -> {
            gameGrid.requestFocus();
            gameGrid.setOnKeyTyped(event -> {
                if (gameOver) return;
                String key = event.getCharacter().toUpperCase();

                if ("\r".equals(key)) { // Enter
                    if (currentGuess.length() == wordLength) submitGuess();
                } else if ("\b".equals(key)) { // Backspace
                    removeLetter();
                } else if (key.matches("[A-ZĄĆĘŁŃÓŚŻŹ]") && key.length() == 1 && currentCol < wordLength && currentRow < maxTries) {
                    addLetter(key);
                }
            });
        });
        timerLabel.setText("Pozostały czas: 60 sekund");
        if (time != null) time.stop();
        time = new Time(60, () -> {
            Platform.runLater(() -> timerLabel.setText("Pozostały czas: " + time.timeSecondsProperty().get() + " sekund"));
        }, this::onTimeUp);
        time.start();
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

    private void handleKeyboardInput(Button keyboardButton) {
        if (gameOver) return;

        String key = keyboardButton.getText();

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
        if (currentCol > 0 && !currentGuess.isEmpty()) {
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
            int hintsUsed = playerScore.getHintCount(playerName);
            int finalScore = playerScore.calculateFinalScore(playerName, currentRow + 1, maxTries);
            playerScore.saveScore(playerName, finalScore, currentRow + 1, hintsUsed, finalScore);
        } else {
            currentRow++;
            currentCol = 0;
            currentGuess = new StringBuilder();

            if (currentRow >= maxTries) {
                showAlert("Przegrana", "Nie udało się! Szukane słowo to: " + targetWord);
                gameOver = true;
                int hintsUsed = playerScore.getHintCount(playerName);
                int finalScore = playerScore.calculateFinalScore(playerName, maxTries, maxTries);
                playerScore.saveScore(playerName, finalScore, maxTries, hintsUsed, finalScore);
            }
        }
    }
    private void colorGuessRow(String guess) {
        guess = guess.toUpperCase();
        String answer = targetWord.toUpperCase();

        boolean[] answerUsed = new boolean[wordLength];
        boolean[] guessGreen = new boolean[wordLength];

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
        rulesButton.setOnAction(e ->  {
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
        });
        highScoresButton.setOnAction(e -> startPanelController.showHighScores());
        backButton.setOnAction(e -> Platform.exit());
        hintButton.setOnAction(e -> {
            if (targetWord == null || targetWord.isEmpty()) {
                showInfo("Podpowiedź", "Słowo jeszcze nie zostało wylosowane.");
            } else {
                showInfo("Podpowiedź", "Pierwsza litera: " + targetWord.charAt(0));
            }
        });
    }
    private void onTimeUp() {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Koniec czasu");
            alert.setHeaderText(null);
            alert.setContentText("Czas na zgadywanie minął!");
            alert.initOwner(rootPane.getScene().getWindow());
            alert.showAndWait();
            gameOver = true;

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
    public void setNick(String nick) {
        this.playerName = nick;
    }
    public void setStartPanelController(StartPanelController startPanelController) {
        this.startPanelController = startPanelController;
    }
    private void showInfo(String title, String text) {
        showAlert(title, text);
    }
}