package org.example.zgadnij_slowo;

import java.util.Scanner;

public class Game {
    private WordDatabase wordDatabase;
    private Player player;
    private ScoreManager scoreManager;
    private String targetWord;
    private int attemptsLeft;
    private boolean gameWon;
    private Scanner scanner;

    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String RED = "\u001B[31m";
    public static final String RESET = "\u001B[0m";
    public static final String BOLD = "\u001B[1m";

    public Game() {
        this.wordDatabase = new WordDatabase();
        this.player = new Player();
        this.scoreManager = new ScoreManager();
        this.scanner = new Scanner(System.in);
        this.attemptsLeft = 6;
        this.gameWon = false;
    }

    public void startGame() {
        System.out.println(BOLD + "=========='ZGADNIJ SŁOWO'==========" + RESET);
        System.out.println("1. Rozpocznij nową grę");
        System.out.println("2. Wyświetl zasady gry");
        System.out.println("3. Wyświetl najlepsze wyniki");
        System.out.println("4. Wyjdz z gry");
        int choice = getValidChoice(1, 4);
        switch(choice) {
            case 1:
                selectCategory();
                this.targetWord = wordDatabase.getRandomWord().toUpperCase();
                System.out.println("\nSłowo do odgadnięcia ma " + targetWord.length() + " liter.");
                System.out.println("Masz " + attemptsLeft + " prób na odgadnięcie słowa.\n");
                gameLoop();
                endGame();
                break;
            case 2:
                // ...obsługa zasad gry...
                break;
            case 3:
                // ...obsługa wyników...
                break;
            case 4:
                System.out.println("Wyjście z gry.");
                break;
        }
    }

    private void selectCategory() {
        System.out.println("\nWybierz kategorię słów:");
        String[] categories = wordDatabase.getCategories();
        for (int i = 0; i < categories.length; i++) {
            System.out.println((i + 1) + ". " + categories[i]);
        }

        System.out.print("Wybór (1-" + categories.length + "): ");
        int choice = getValidChoice(1, categories.length);
        String selectedCategory = categories[choice - 1];
        // Dynamiczne ładowanie kategorii podczas gry
        String filePath = "src/main/resources/" + selectedCategory + ".txt";
        wordDatabase.loadCategoryFromFile(selectedCategory, filePath);
        wordDatabase.setCurrentCategory(selectedCategory);
    }

    private void gameLoop() {
        while (attemptsLeft > 0 && !gameWon) {
            System.out.println("Pozostałe próby: " + attemptsLeft);
            System.out.print("Wprowadź słowo (" + targetWord.length() + " liter): ");

            String guess = getValidGuess();
            String result = checkGuess(guess);

            System.out.println("Twoja propozycja: " + result);

            if (guess.equals(targetWord)) {
                gameWon = true;
                break;
            }

            attemptsLeft--;
            System.out.println();
        }
    }

    private String getValidGuess() {
        while (true) {
            String input = scanner.nextLine().trim().toUpperCase();

            if (input.length() != targetWord.length()) {
                System.out.print("Słowo musi mieć " + targetWord.length() + " liter. Spróbuj ponownie: ");
                continue;
            }

            if (!input.matches("[A-ZĄĆĘŁŃÓŚŹŻ]+")) {
                System.out.print("Używaj tylko liter. Spróbuj ponownie: ");
                continue;
            }

            return input;
        }
    }

    private String checkGuess(String guess) {
        StringBuilder result = new StringBuilder();
        boolean[] usedInTarget = new boolean[targetWord.length()];

        // Zielone (trafione miejsce)
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == targetWord.charAt(i)) {
                result.append(GREEN).append(guess.charAt(i)).append(RESET);
                usedInTarget[i] = true;
            } else {
                result.append("_"); // tymczasowy znak, nadpiszemy później jeśli żółty
            }
        }

        // Żółte (litera jest, ale nie tu)
        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) != targetWord.charAt(i)) {
                boolean found = false;
                for (int j = 0; j < targetWord.length(); j++) {
                    if (!usedInTarget[j] && guess.charAt(i) == targetWord.charAt(j)) {
                        found = true;
                        usedInTarget[j] = true;
                        break;
                    }
                }
                if (found) {
                    // Zamień "_" na żółty znak
                    int start = result.indexOf("_", i);
                    result.replace(start, start + 1, YELLOW + guess.charAt(i) + RESET);
                } else {
                    // Zamień "_" na zwykłą literę (nie ma w słowie)
                    int start = result.indexOf("_", i);
                    result.replace(start, start + 1, String.valueOf(guess.charAt(i)));
                }
            }
        }
        return result.toString();
    }

    private int getValidChoice(int min, int max) {
        while (true) {
            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice >= min && choice <= max) {
                    return choice;
                }
                System.out.print("Wybierz liczbę od " + min + " do " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Wprowadź poprawną liczbę: ");
            }
        }
    }

    private void endGame() {
        if (gameWon) {
            System.out.println(GREEN + BOLD + "\n GRATULACJE! Odgadłeś słowo: " + targetWord + RESET);
            int score = calculateScore();
            player.addScore(score);
            scoreManager.saveScore(player.getName(), score);
            System.out.println("Twój wynik: " + score + " punktów");
        } else {
            System.out.println(RED + BOLD + "\n Koniec gry! Słowo to: " + targetWord + RESET);
        }

        scoreManager.displayHighScores();

        System.out.print("\nChcesz zagrać ponownie? (t/n): ");
        if (scanner.nextLine().trim().toLowerCase().startsWith("t")) {
            resetGame();
            startGame();
        }
    }

    private int calculateScore() {
        int baseScore = 100;
        int usedAttempts = 6 - attemptsLeft;
        return Math.max(10, baseScore - (usedAttempts * 15));
    }

    private void resetGame() {
        this.attemptsLeft = 6;
        this.gameWon = false;
        this.targetWord = null;
    }
}
