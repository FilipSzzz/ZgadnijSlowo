module org.example.zgadnij_slowo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.bootstrapfx.core;


    opens org.example.zgadnij_slowo to javafx.fxml;
    exports org.example.zgadnij_slowo;
}