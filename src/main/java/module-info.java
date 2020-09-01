module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    opens org.example.controller to javafx.fxml;
    opens org.example.models to javafx.base;
    opens org.example.database to org.example.models;
    exports org.example;
}