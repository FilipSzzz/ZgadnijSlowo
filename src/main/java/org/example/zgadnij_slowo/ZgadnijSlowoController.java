package org.example.zgadnij_slowo;

import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class ZgadnijSlowoController implements Initializable {

    private String difficulty;
    private String category;
    private WordDatabase wordDatabase;
    private StartPanelController startPanelController;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        wordDatabase = new WordDatabase();
    }

    public void setCategory(String category) {
        this.category = category;
        setupGameForCategoryAndDifficulty();
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
        setupGameForCategoryAndDifficulty();
    }

    private void setupGameForCategoryAndDifficulty() {
        if (category != null && difficulty != null) {
            wordDatabase.setCategoryAndDifficulty(category, difficulty);
        }
    }
}
