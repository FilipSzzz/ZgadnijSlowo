package org.example.zgadnij_slowo;

import java.util.*;
import java.io.*;

public class WordDatabase {
    private Map<String, List<String>> categories;
    private String currentCategory;
    private Random random;

    public WordDatabase() {
        this.categories = new HashMap<>();
        this.random = new Random();
        loadCategoryFromFile("jedzenie6liter", getClass().getResource("/jedzenie6liter.txt").getPath());
        loadCategoryFromFile("kolory6liter", getClass().getResource("/kolory6liter.txt").getPath());
        loadCategoryFromFile("kraje6liter", getClass().getResource("/kraje6liter.txt").getPath());
        loadCategoryFromFile("zawody6liter", getClass().getResource("/zawody6liter.txt").getPath());
        loadCategoryFromFile("zwierzeta6liter", getClass().getResource("/zwierzeta6liter.txt").getPath());

        loadCategoryFromFile("zwierzeta7liter", getClass().getResource("/zwierzeta7liter.txt").getPath());
        loadCategoryFromFile("zawody7liter", getClass().getResource("/zawody7liter.txt").getPath());
        loadCategoryFromFile("kraje7liter", getClass().getResource("/kraje7liter.txt").getPath());
        loadCategoryFromFile("kolory7liter", getClass().getResource("/kolory7liter.txt").getPath());
        loadCategoryFromFile("jedzenie7liter", getClass().getResource("/jedzenie7liter.txt").getPath());
        loadCategoryFromFile("wszystkie7liter", getClass().getResource("/wszystkie7liter.txt").getPath());
    }
    public void loadCategoryFromFile(String categoryName, String filePath) {
    List<String> words = new ArrayList<>();
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
        String line;
        while ((line = reader.readLine()) != null) {
            line = line.trim();
            if (!line.isEmpty()) {
                words.add(line);
            }
        }
        categories.put(categoryName, words);
    } catch (IOException e) {
        System.err.println("Błąd podczas wczytywania kategorii z pliku '" + filePath + "': " + e.getMessage());
    }
}

    public void setCategoryAndDifficulty(String category, String difficulty) {
        String key = category.toLowerCase() + (difficulty.equalsIgnoreCase("łatwy") ? "6liter" : "7liter");
        setCurrentCategory(key);
    }

    public String[] getCategories() {
        return categories.keySet().toArray(new String[0]);
    }

    public void setCurrentCategory(String category) {
        if (categories.containsKey(category)) {
            this.currentCategory = category;
        }
    }

    public String getRandomWord() {
        List<String> words = categories.get(currentCategory);
        if (words == null || words.isEmpty()) {
            return "SŁOWO";
        }
        return words.get(random.nextInt(words.size()));
    }

    public String getCurrentCategory() {
        return currentCategory;
    }
}