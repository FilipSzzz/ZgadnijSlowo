package org.example.zgadnij_slowo;

public abstract class Score {
    public abstract int calculateFinalScore(String playerName, int attemptsUsed, int maxAttempts);
    public abstract void saveScore(String playerName, int score, int attemptsPenalty, int hintsUsed, int finalScore);
}