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
        loadCategoryFromResource("wszystkie6liter", "/wszystkie6liter.txt", 6);
        loadCategoryFromResource("zawody6liter", "/zawody6liter.txt", 6);
        loadCategoryFromResource("zawody7liter", "/zawody7liter.txt", 7);
        loadCategoryFromResource("zwierzeta6liter", "/zwierzeta6liter.txt", 6);
        loadCategoryFromResource("zwierzeta7liter", "/zwierzeta7liter.txt", 7);


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
        // Jeśli user wybrał już kategorię zawierającą 6liter/7liter, nie doklejaj
        if (category.endsWith("6liter") || category.endsWith("7liter")) {
            setCurrentCategory(category);
        } else {
            String key = category.toLowerCase() + (difficulty.equalsIgnoreCase("trudny") ? "7liter" : "6liter");
            setCurrentCategory(key);
        }
        System.out.println("DEBUG: Ustawiam kategorię: " + currentCategory);
        System.out.println("DEBUG: Słowa w tej kategorii: " + categories.get(currentCategory));
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
            // Domyślnie wróć właściwej długości pusty string (lepiej: rzucić wyjątek)
            if (currentCategory != null && currentCategory.endsWith("6liter")) {
                return "??????"; // 6 znaków
            } else if (currentCategory != null && currentCategory.endsWith("7liter")) {
                return "???????"; // 7 znaków
            } else {
                return "??????"; // domyślnie 6
            }
        }
        return words.get(random.nextInt(words.size()));
    }


    public String getCurrentCategory() {
        return currentCategory;
    }
}