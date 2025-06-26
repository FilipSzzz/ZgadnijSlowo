package org.example.zgadnij_slowo.Score;

public abstract class Score {
    public abstract int calculateFinalScore(String playerName, int attemptsUsed, int maxAttempts);
    public abstract void saveScore(String playerName, int score, int attemptsPenalty, int hintsUsed, int finalScore, int timeTaken);
}