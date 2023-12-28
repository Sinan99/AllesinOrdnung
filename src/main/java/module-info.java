module com.example.allesinordnung {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;

    opens com.example.allesinordnung to javafx.fxml;
    exports com.example.allesinordnung;
}