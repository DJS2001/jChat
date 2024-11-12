module com.example.modelo {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.xerial.sqlitejdbc;
    requires java.desktop;


    opens com.example.modelo to javafx.fxml;
    exports com.example.modelo;
    exports com.example.controlador;
    opens com.example.controlador to javafx.fxml;
}