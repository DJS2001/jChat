package com.example.controlador;

import javafx.application.Platform;
import javafx.scene.control.Alert;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ExceptionHandler {

    private static final Logger logger = Logger.getLogger(Servidor.class.getName());

    public static void manejarError(String mensaje, Throwable causa) {
        mostrarErrorAlUsuario(mensaje);

        logger.severe("Error: " + mensaje);
        logger.log(Level.SEVERE, causa.getMessage(), causa);
    }


    public static void mostrarErrorAlUsuario(String mensaje) {
        Platform.runLater(() -> {
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Error");
            alerta.setHeaderText("Ocurrió un problema");
            alerta.setContentText(mensaje);
            alerta.showAndWait();
        });
    }

}
