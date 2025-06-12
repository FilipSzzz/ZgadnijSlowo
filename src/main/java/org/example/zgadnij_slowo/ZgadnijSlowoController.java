package org.example.zgadnij_slowo;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

import java.net.URL;
import java.util.*;

public class ZgadnijSlowoController implements Initializable {

    @FXML private GridPane gameGrid;
    @FXML private VBox keyboardContainer;
    @FXML private Label messageLabel;
    @FXML private Button newGameButton;
    @FXML private Label titleLabel;
    @FXML private Button gameModeButton;
    @FXML private Button rulesButton;
    @FXML private Button highScoresButton;
    @FXML private Button exitButton;

    private static final int GRID_SIZE = 6;
    private static final int WORD_LENGTH = 5;
    WordDatabase wordDatabase = new WordDatabase();
    private String targetWord;
    private int currentRow = 0;
    private int currentCol = 0;
    private Label[][] gridLabels;
    private Map<String, Button> keyboardButtons;
    private boolean gameOver = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupStyling();
        initializeGame();
        setupKeyboard();
        setupEventHandlers();
        setupButtonHandlers();
    }

    private void setupStyling() {
        // Style the main container
        titleLabel.setFont(Font.font("Arial", FontWeight.BOLD, 32));
        titleLabel.setTextFill(Color.WHITE);
        titleLabel.setAlignment(Pos.CENTER);

        messageLabel.setFont(Font.font("Arial", 16));
        messageLabel.setTextFill(Color.WHITE);
        messageLabel.setAlignment(Pos.CENTER);

        newGameButton.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        newGameButton.setTextFill(Color.WHITE);
        newGameButton.setStyle("-fx-background-color: #6aaa64; -fx-background-radius: 6;");
        newGameButton.setPrefSize(120, 40);

        // Style the game grid
        gameGrid.setAlignment(Pos.CENTER);
        gameGrid.setHgap(5);
        gameGrid.setVgap(5);
        gameGrid.setStyle("-fx-background-color: #121213;");

        keyboardContainer.setAlignment(Pos.CENTER);
        keyboardContainer.setSpacing(8);
    }

    private void initializeGame() {
        Random random = new Random();
        targetWord = wordDatabase.getRandomWord().toUpperCase();
        System.out.println("Target word: " + targetWord); // For debugging

        // Initialize grid
        gridLabels = new Label[GRID_SIZE][WORD_LENGTH];
        gameGrid.getChildren().clear();

        for (int row = 0; row < GRID_SIZE; row++) {
            for (int col = 0; col < WORD_LENGTH; col++) {
                Label label = new Label();
                styleGridCell(label, "empty");
                gridLabels[row][col] = label;
                gameGrid.add(label, col, row);
            }
        }

        currentRow = 0;
        currentCol = 0;
        gameOver = false;
        messageLabel.setText("Guess the 5-letter word!");
    }

    private void styleGridCell(Label label, String state) {
        label.setPrefSize(60, 60);
        label.setAlignment(Pos.CENTER);
        label.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        label.setTextFill(Color.WHITE);

        switch (state) {
            case "empty":
                label.setStyle("-fx-background-color: #121213; -fx-border-color: #3a3a3c; -fx-border-width: 2;");
                break;
            case "filled":
                label.setStyle("-fx-background-color: #121213; -fx-border-color: #565758; -fx-border-width: 2;");
                break;
            case "correct":
                label.setStyle("-fx-background-color: #6aaa64; -fx-border-color: #6aaa64; -fx-border-width: 2;");
                break;
            case "wrong-position":
                label.setStyle("-fx-background-color: #c9b458; -fx-border-color: #c9b458; -fx-border-width: 2;");
                break;
            case "wrong":
                label.setStyle("-fx-background-color: #787c7e; -fx-border-color: #787c7e; -fx-border-width: 2;");
                break;
        }
    }

    private void setupKeyboard() {
        keyboardButtons = new HashMap<>();
        String[] keyboardRows = {
                "QWERTYUIOP",
                "ASDFGHJKL",
                "ZXCVBNM"
        };

        keyboardContainer.getChildren().clear();

        for (String row : keyboardRows) {
            HBox keyRow = new HBox();
            keyRow.setAlignment(Pos.CENTER);
            keyRow.setSpacing(6);

            for (char c : row.toCharArray()) {
                Button button = new Button(String.valueOf(c));
                styleKeyboardButton(button, "normal");
                button.setOnAction(e -> handleKeyPress(String.valueOf(c)));
                keyboardButtons.put(String.valueOf(c), button);
                keyRow.getChildren().add(button);
            }

            keyboardContainer.getChildren().add(keyRow);
        }

        // Add special keys
        HBox specialRow = new HBox();
        specialRow.setAlignment(Pos.CENTER);
        specialRow.setSpacing(6);

        Button enterButton = new Button("ENTER");
        styleKeyboardButton(enterButton, "special");
        enterButton.setOnAction(e -> handleEnter());

        Button backspaceButton = new Button("⌫");
        styleKeyboardButton(backspaceButton, "special");
        backspaceButton.setOnAction(e -> handleBackspace());

        specialRow.getChildren().addAll(enterButton, backspaceButton);
        keyboardContainer.getChildren().add(specialRow);
    }

    private void styleKeyboardButton(Button button, String type) {
        button.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        button.setTextFill(Color.WHITE);
        button.setPrefHeight(50);

        if (type.equals("special")) {
            button.setPrefWidth(60);
            button.setFont(Font.font("Arial", FontWeight.BOLD, 12));
        } else {
            button.setPrefWidth(35);
        }

        button.setStyle("-fx-background-color: #818384; -fx-background-radius: 4;");

        // Add hover effect
        button.setOnMouseEntered(e -> {
            if (!button.getStyle().contains("#6aaa64") &&
                    !button.getStyle().contains("#c9b458") &&
                    !button.getStyle().contains("#3a3a3c")) {
                button.setStyle("-fx-background-color: #9a9a9a; -fx-background-radius: 4;");
            }
        });

        button.setOnMouseExited(e -> {
            if (!button.getStyle().contains("#6aaa64") &&
                    !button.getStyle().contains("#c9b458") &&
                    !button.getStyle().contains("#3a3a3c")) {
                button.setStyle("-fx-background-color: #818384; -fx-background-radius: 4;");
            }
        });
    }

    private void updateKeyboardButtonStyle(Button button, String state) {
        switch (state) {
            case "correct":
                button.setStyle("-fx-background-color: #6aaa64; -fx-background-radius: 4;");
                break;
            case "wrong-position":
                button.setStyle("-fx-background-color: #c9b458; -fx-background-radius: 4;");
                break;
            case "wrong":
                button.setStyle("-fx-background-color: #3a3a3c; -fx-background-radius: 4;");
                break;
        }
    }

    private void setupEventHandlers() {
        newGameButton.setOnAction(e -> {
            initializeGame();
            setupKeyboard(); // Reset keyboard colors
        });

        // Handle keyboard input - need to set this after scene is available
        newGameButton.getScene().setOnKeyPressed(this::handleKeyboardInput);
    }

    private void handleKeyboardInput(KeyEvent event) {
        if (gameOver) return;

        String key = event.getCode().toString();

        if (key.matches("[A-Z]") && key.length() == 1) {
            handleKeyPress(key);
        } else if (key.equals("ENTER")) {
            handleEnter();
        } else if (key.equals("BACK_SPACE")) {
            handleBackspace();
        }
    }

    private void handleKeyPress(String letter) {
        if (gameOver || currentCol >= WORD_LENGTH) return;

        gridLabels[currentRow][currentCol].setText(letter);
        styleGridCell(gridLabels[currentRow][currentCol], "filled");
        currentCol++;
    }

    private void handleBackspace() {
        if (gameOver || currentCol == 0) return;

        currentCol--;
        gridLabels[currentRow][currentCol].setText("");
        styleGridCell(gridLabels[currentRow][currentCol], "empty");
    }

    private void handleEnter() {
        if (gameOver || currentCol != WORD_LENGTH) return;

        // Get current guess
        StringBuilder guess = new StringBuilder();
        for (int i = 0; i < WORD_LENGTH; i++) {
            guess.append(gridLabels[currentRow][i].getText());
        }

        String guessWord = guess.toString();

        // Check if word is in dictionary (simplified - just check if it's 5 letters)
        if (guessWord.length() != WORD_LENGTH) {
            messageLabel.setText("Word must be 5 letters!");
            return;
        }

        // Evaluate guess
        evaluateGuess(guessWord);

        // Check win condition
        if (guessWord.equals(targetWord)) {
            messageLabel.setText("Congratulations! You won!");
            gameOver = true;
            return;
        }

        // Move to next row
        currentRow++;
        currentCol = 0;

        // Check lose condition
        if (currentRow >= GRID_SIZE) {
            messageLabel.setText("Game Over! The word was: " + targetWord);
            gameOver = true;
        }
    }

    private void evaluateGuess(String guess) {
        // Count letter frequencies in target word
        Map<Character, Integer> targetFreq = new HashMap<>();
        for (char c : targetWord.toCharArray()) {
            targetFreq.put(c, targetFreq.getOrDefault(c, 0) + 1);
        }

        // First pass: mark correct positions (green)
        boolean[] processed = new boolean[WORD_LENGTH];
        for (int i = 0; i < WORD_LENGTH; i++) {
            char guessChar = guess.charAt(i);
            char targetChar = targetWord.charAt(i);

            if (guessChar == targetChar) {
                styleGridCell(gridLabels[currentRow][i], "correct");
                Button keyButton = keyboardButtons.get(String.valueOf(guessChar));
                if (keyButton != null) {
                    updateKeyboardButtonStyle(keyButton, "correct");
                }
                targetFreq.put(guessChar, targetFreq.get(guessChar) - 1);
                processed[i] = true;
            }
        }

        // Second pass: mark wrong positions (yellow) and wrong letters (gray)
        for (int i = 0; i < WORD_LENGTH; i++) {
            if (processed[i]) continue;

            char guessChar = guess.charAt(i);
            Button keyButton = keyboardButtons.get(String.valueOf(guessChar));

            if (targetFreq.getOrDefault(guessChar, 0) > 0) {
                styleGridCell(gridLabels[currentRow][i], "wrong-position");
                if (keyButton != null && !keyButton.getStyle().contains("#6aaa64")) {
                    updateKeyboardButtonStyle(keyButton, "wrong-position");
                }
                targetFreq.put(guessChar, targetFreq.get(guessChar) - 1);
            } else {
                styleGridCell(gridLabels[currentRow][i], "wrong");
                if (keyButton != null &&
                        !keyButton.getStyle().contains("#6aaa64") &&
                        !keyButton.getStyle().contains("#c9b458")) {
                    updateKeyboardButtonStyle(keyButton, "wrong");
                }
            }
        }
    }

    private void setupButtonHandlers() {
        // Obsługa przycisku zmiany trybu gry
        gameModeButton.setOnAction(event -> {
            System.out.println("Zmiana trybu gry");
            // Tu dodaj kod do zmiany trybu gry
        });

        // Obsługa przycisku zasad gry
        rulesButton.setOnAction(event -> {
            System.out.println("Wyświetlanie zasad gry");
            // Tu dodaj kod do wyświetlania zasad gry
        });

        // Obsługa przycisku najlepszych wyników
        highScoresButton.setOnAction(event -> {
            System.out.println("Wyświetlanie najlepszych wyników");
            // Tu dodaj kod do wyświetlania najlepszych wyników
        });

        // Obsługa przycisku wyjścia
        exitButton.setOnAction(event -> {
            System.out.println("Wyjście z gry");
            Platform.exit();
        });
    }

    // Dodaj tę metodę do istniejącego kontrolera WordleController
    public void setDifficulty(String difficulty) {
        System.out.println("Ustawiono poziom trudności: " + difficulty);
        // Tutaj możesz dostosować logikę gry w zależności od wybranego poziomu trudności

        // Przykładowa implementacja:
        switch (difficulty) {
            case "Łatwy":
                // Np. więcej podpowiedzi, łatwiejsze słowa
                break;
            case "Średni":
                // Standardowe zasady
                break;
            case "Trudny":
                // Np. odkryte litery muszą być użyte w kolejnych próbach
                break;
        }
    }
}
