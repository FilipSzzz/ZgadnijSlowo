package org.example.zgadnij_slowo;

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
        loadCategoryFromResource("wszystkie7liter", "/wszystkie7liter.txt", 7);

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
            System.out.println("Załadowano do " + categoryName + ": " + words);

            categories.put(categoryName, words);
        } catch (IOException | NullPointerException e) {
            System.err.println("Błąd podczas wczytywania zasobu '" + resourcePath + "': " + e.getMessage());
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