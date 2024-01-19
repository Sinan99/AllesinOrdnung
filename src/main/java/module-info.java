module com.example.allesinordnung {
    requires javafx.controls;
    requires javafx.fxml;

    requires com.dlsc.formsfx;
    requires com.fasterxml.jackson.databind;
    requires org.controlsfx.controls; // Corrected module name

    opens com.example.allesinordnung to javafx.fxml;
    exports com.example.allesinordnung;
}
