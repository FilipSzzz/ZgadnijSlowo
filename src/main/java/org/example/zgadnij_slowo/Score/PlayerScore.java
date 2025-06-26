package org.example.zgadnij_slowo.Score;

import org.example.zgadnij_slowo.StartPanelController;


import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class PlayerScore extends Score {
    private Map<String, Integer> playerHintUsage = new HashMap<>();
    private static final int BASE_SCORE = 100;
    private static final int HINT_PENALTY = 10;
    private static final int MIN_SCORE = 5;
    private static final int TIME_PENALTY = 1;
    private static final String SCORE_FILE = "scores.txt";
    private StartPanelController startPanelController;

    public PlayerScore() {}

    public void registerHintUsage(String playerName) {
        playerHintUsage.put(playerName, playerHintUsage.getOrDefault(playerName, 0) + 1);
    }

    @Override
    public int calculateFinalScore(String playerName, int attemptsUsed, int maxAttempts) {
        int hintsUsed = playerHintUsage.getOrDefault(playerName, 0);
        int score = BASE_SCORE - (hintsUsed * HINT_PENALTY);
        return Math.max(MIN_SCORE, score);
    }

    public int getHintCount(String playerName) {
        return playerHintUsage.getOrDefault(playerName, 0);
    }


    @Override
    public void saveScore(String playerName, int score, int attemptsPenalty, int hintsUsed, int finalScore, int timeTaken) {
        try(FileWriter writer = new FileWriter(SCORE_FILE, true)){
            writer.write(String.format("%s: %d pkt (Kara za próby: %d pkt, Użyte podpowiedzi: %d, Kara za podpowiedzi: %d pkt Czas: %d)\n",
                    playerName, finalScore, attemptsPenalty, hintsUsed, hintsUsed * HINT_PENALTY, timeTaken));
        } catch (Exception e) {
            System.err.println("Błąd podczas zapisywania wyniku: " + e.getMessage());
        }
    }
}