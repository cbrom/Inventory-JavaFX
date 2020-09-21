module org.example {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.google.gson;
    requires org.apache.httpcomponents.httpclient;
    requires org.apache.httpcomponents.httpcore;
    requires org.apache.commons.codec;
    requires org.apache.httpcomponents.httpmime;

    opens org.example.controller to javafx.fxml;
    opens org.example.models to javafx.base;
    opens org.example.database to org.example.models;
    exports org.example;
}