package org.example.zgadnij_slowo;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private String name;
    private int totalScore;
    private int gamesPlayed;
    private int gamesWon;
    private List<Integer> gameScores;

    public Player() {
        this.name = "Gracz";
        this.totalScore = 0;
        this.gamesPlayed = 0;
        this.gamesWon = 0;
        this.gameScores = new ArrayList<>();
    }

    public Player(String name) {
        this();
        this.name = name;
    }

    public void addScore(int score) {
        this.totalScore += score;
        this.gamesPlayed++;
        this.gamesWon++;
        this.gameScores.add(score);
    }

    public void addGame() {
        this.gamesPlayed++;
    }

    public double getAverageScore() {
        if (gamesPlayed == 0) return 0.0;
        return (double) totalScore / gamesPlayed;
    }

    public double getWinRate() {
        if (gamesPlayed == 0) return 0.0;
        return (double) gamesWon / gamesPlayed * 100;
    }

    public int getBestScore() {
        if (gameScores.isEmpty()) return 0;
        return gameScores.stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    public void displayStats() {
        System.out.println("\n=== STATYSTYKI GRACZA ===");
        System.out.println("Imię: " + name);
        System.out.println("Rozegrane gry: " + gamesPlayed);
        System.out.println("Wygrane gry: " + gamesWon);
        System.out.println("Procent wygranych: " + String.format("%.1f%%", getWinRate()));
        System.out.println("Łączne punkty: " + totalScore);
        System.out.println("Średnia punktów: " + String.format("%.1f", getAverageScore()));
        System.out.println("Najlepszy wynik: " + getBestScore());
    }

    // Gettery i settery
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getTotalScore() { return totalScore; }
    public int getGamesPlayed() { return gamesPlayed; }
    public int getGamesWon() { return gamesWon; }
    public List<Integer> getGameScores() { return new ArrayList<>(gameScores); }
}
