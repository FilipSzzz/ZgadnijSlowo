package org.example.zgadnij_slowo.Database;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;

public class WordDatabase {
    private Map<String, List<String>> categories;
    private String currentCategory;
    private Random random;

    public WordDatabase() {
        this.categories = new HashMap<>();
        this.random = new Random();

        loadCategoryFromResource("jedzenie6liter", "/jedzenie6liter.txt", 6);
        loadCategoryFromResource("jedzenie7liter", "/jedzenie7liter.txt", 7);
        loadCategoryFromResource("kraje6liter", "/kraje6liter.txt", 6);
        loadCategoryFromResource("kraje7liter", "/kraje7liter.txt", 7);
        loadCategoryFromResource("kolory6liter", "/kolory6liter.txt", 6);
        loadCategoryFromResource("kolory7liter", "/kolory7liter.txt", 7);
        loadCategoryFromResource("kolory5liter", "/kolory5liter.txt", 8);
        loadCategoryFromResource("wszystkie7liter", "/wszystkie7liter.txt", 7);
        loadCategoryFromResource("wszystkie6liter", "/wszystkie6liter.txt", 6);
        loadCategoryFromResource("zawody6liter", "/zawody6liter.txt", 6);
        loadCategoryFromResource("zawody7liter", "/zawody7liter.txt", 7);
        loadCategoryFromResource("zwierzeta6liter", "/zwierzeta6liter.txt", 6);
        loadCategoryFromResource("zwierzeta7liter", "/zwierzeta7liter.txt", 7);
        loadCategoryFromResource("jedzenie5liter","/jedzenie5liter.txt", 5);
        loadCategoryFromResource("kraje5liter","/kraje5liter.txt", 5);
        loadCategoryFromResource("wszystkie5liter","/wszystkie5liter.txt", 5);
        loadCategoryFromResource("zwierzeta5liter","/zwierzeta5liter.txt", 5);
        loadCategoryFromResource("zawody5liter","/zawody5liter.txt", 5);


    }

    public void loadCategoryFromResource(String categoryName, String resourcePath, int expectedLength) {
        List<String> words = new ArrayList<>();
        try (InputStream in = getClass().getResourceAsStream(resourcePath);
             BufferedReader reader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] splitWords = line.trim().split("[,\\s]+"); // przecinek lub spacja
                for (String word : splitWords) {
                    word = word.trim();
                    if (!word.isEmpty() && word.length() == expectedLength) {
                        words.add(word);

                    }
                }
            }
            categories.put(categoryName, words);
        } catch (IOException | NullPointerException e) {
            showAlert("Błąd ładowania kategorii: " + categoryName, "Nie można załadować słów z pliku: " + resourcePath);
        }
    }

    public void setCategoryAndDifficulty(String category, String difficulty) {
        if (category == null || difficulty == null) {
            return;
        }
        String normalizedCategory = category.toLowerCase();
        String key;
        if (normalizedCategory.endsWith("5liter") ||
            normalizedCategory.endsWith("6liter") ||
            normalizedCategory.endsWith("7liter")) {
            key = normalizedCategory;
        } else {
            int length;
            if (difficulty.equalsIgnoreCase("łatwy") || difficulty.equals("5")) {
                length = 5;
            } else if (difficulty.equalsIgnoreCase("trudny") || difficulty.equalsIgnoreCase("7liter") || difficulty.equals("7")) {
                length = 7;
            } else {
                length = 6;
            }
            key = normalizedCategory + length + "liter";
        }
        setCurrentCategory(key);
    }
    public void setCurrentCategory(String category) {
        if (categories.containsKey(category)) {
                this.currentCategory = category;
        }
    }

    public String getRandomWord() {
        List<String> words = categories.get(currentCategory);
        if (words == null || words.isEmpty()) {
            showAlert("BRAK SŁÓW","Nie znaleziono słow w tej kategorii");
            return null;
        }
        return words.get(random.nextInt(words.size()));
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
}