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
        loadCategoryFromFile("wszystkie","src/main/resources/wszystkie.txt");
        loadCategoryFromFile("zawody","src/main/resources/zawody.txt");
        loadCategoryFromFile("zwierzeta","src/main/resources/zwierzeta.txt");
        loadCategoryFromFile("kolory","src/main/resources/kolory.txt");
        loadCategoryFromFile("kraje","src/main/resources/kraje.txt");
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