module org.example.zgadnij_slowo {
    requires javafx.controls;
    requires javafx.fxml;
    opens org.example.zgadnij_slowo to javafx.fxml;
    exports org.example.zgadnij_slowo;
    exports org.example.zgadnij_slowo.Score;
    opens org.example.zgadnij_slowo.Score to javafx.fxml;
    exports org.example.zgadnij_slowo.Database;
    opens org.example.zgadnij_slowo.Database to javafx.fxml;
}