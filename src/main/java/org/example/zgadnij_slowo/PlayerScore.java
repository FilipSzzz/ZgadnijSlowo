package org.example.zgadnij_slowo;

import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;

public class PlayerScore extends Score {
    private Map<String, Integer> playerHintUsage = new HashMap<>();
    private static final int BASE_SCORE = 100;
    private static final int HINT_PENALTY = 10;
    private static final int MIN_SCORE = 5;
    private static final String SCORE_FILE = "scores.txt";
    private StartPanelController startPanelController;

    public PlayerScore() {}

    public void registerHintUsage(String playerName) {
        playerHintUsage.put(playerName, playerHintUsage.getOrDefault(playerName, 0) + 1);
        System.out.println("Gracz " + playerName + " użył podpowiedzi. Łączna liczba: " +
                playerHintUsage.get(playerName));
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

    public void resetPlayerHints(String playerName) {
        playerHintUsage.put(playerName, 0);
    }

    public void displayScoreBreakdown(String playerName, int attemptsUsed, int maxAttempts) {
        int hintsUsed = getHintCount(playerName);
        int attemptPenalty = attemptsUsed;
        int hintPenalty = hintsUsed * HINT_PENALTY;
        int finalScore = calculateFinalScore(playerName, attemptsUsed, maxAttempts);

        System.out.println("\n=== BREAKDOWN PUNKTACJI ===");
        System.out.println("Gracz: " + playerName);
        System.out.println("Bazowy wynik: " + BASE_SCORE + " pkt");
        System.out.println("Wykorzystane próby: " + attemptsUsed + "/" + maxAttempts +
                " (-" + attemptPenalty + " pkt)");
        System.out.println("Użyte podpowiedzi: " + hintsUsed + " (-" + hintPenalty + " pkt)");
        System.out.println("KOŃCOWY WYNIK: " + finalScore + " pkt");
    }

    public void setHintPenalty(int hintPenalty) {
        System.out.println("Kara za podpowiedź ustawiona na: " + hintPenalty + " pkt");
    }

    @Override
    public void saveScore(String playerName, int score, int attemptsPenalty, int hintsUsed, int finalScore) {
        try(FileWriter writer = new FileWriter(SCORE_FILE, true)){
            writer.write(String.format("%s: %d pkt (Kara za próby: %d pkt, Użyte podpowiedzi: %d, Kara za podpowiedzi: %d pkt)\n",
                    playerName, finalScore, attemptsPenalty, hintsUsed, hintsUsed * HINT_PENALTY));
        } catch (Exception e) {
            System.err.println("Błąd podczas zapisywania wyniku: " + e.getMessage());
        }
    }
}