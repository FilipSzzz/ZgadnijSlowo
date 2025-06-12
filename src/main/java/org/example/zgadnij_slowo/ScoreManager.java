package org.example.zgadnij_slowo;

import java.io.*;
import java.util.*;

public class ScoreManager {
    private static final String HIGH_SCORES_FILE = "highscores.txt";
    private Map<String, Integer> highScores;

    public ScoreManager() {
        this.highScores = new HashMap<>();
        loadHighScores();
    }

    public void saveScore(String playerName, int score) {
        // Aktualizuj najlepszy wynik gracza jeśli nowy jest lepszy
        if (!highScores.containsKey(playerName) || highScores.get(playerName) < score) {
            highScores.put(playerName, score);
            saveHighScoresToFile();
        }
    }

    private void saveHighScoresToFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(HIGH_SCORES_FILE))) {
            for (Map.Entry<String, Integer> entry : highScores.entrySet()) {
                writer.write(entry.getKey() + ":" + entry.getValue());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas zapisywania wyników: " + e.getMessage());
        }
    }

    private void loadHighScores() {
        File file = new File(HIGH_SCORES_FILE);
        if (!file.exists()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2) {
                    try {
                        String name = parts[0];
                        int score = Integer.parseInt(parts[1]);
                        highScores.put(name, score);
                    } catch (NumberFormatException e) {
                        System.err.println("Błędny format wyniku: " + line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Błąd podczas wczytywania wyników: " + e.getMessage());
        }
    }

    public void displayHighScores() {
        if (highScores.isEmpty()) {
            System.out.println("\nBrak zapisanych wyników.");
            return;
        }

        System.out.println("\n=== NAJLEPSZE WYNIKI ===");

        // Sortowanie wyników malejąco
        List<Map.Entry<String, Integer>> sortedScores = new ArrayList<>(highScores.entrySet());
        sortedScores.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        int position = 1;
        for (Map.Entry<String, Integer> entry : sortedScores) {
            if (position <= 10) { // Wyświetl top 10
                System.out.printf("%2d. %-15s %d pkt%n",
                        position, entry.getKey(), entry.getValue());
                position++;
            } else {
                break;
            }
        }
    }

    public int getHighScore(String playerName) {
        return highScores.getOrDefault(playerName, 0);
    }

    public int getOverallHighScore() {
        return highScores.values().stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    public void clearHighScores() {
        highScores.clear();
        File file = new File(HIGH_SCORES_FILE);
        if (file.exists()) {
            file.delete();
        }
    }
}
