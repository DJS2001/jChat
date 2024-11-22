package com.example.controlador;

import com.example.modelo.SQLiteManager;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class MainControlador {
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfContrasenia;
    @FXML
    private Label alert;
    private int puerto = 51005;
    private Stage primaryStage;
    private SQLiteManager manager = new SQLiteManager();

    public void setPrimaryStage(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    @FXML
    public void registro() {
        String nombre = tfName.getText();
        String contrasenia = tfContrasenia.getText();

        if (!tfName.getText().isEmpty() && validacionNombre(tfName)) {
            if (manager.validaCuenta(nombre,contrasenia)) {
                try {
                    String ipLocal = InetAddress.getLocalHost().getHostAddress();
                    Socket socket = new Socket(ipLocal, puerto);
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/vista/chatVista.fxml"));
                    Parent root = fxmlLoader.load();
                    PanelCliente controller = fxmlLoader.getController();
                    controller.inicializarHilos(nombre, socket);

                    Scene scene = new Scene(root);
                    scene.getStylesheets().add(getClass().getResource("/com/example/vista/estilos.css").toExternalForm());

                    Stage stage = new Stage();
                    stage.setScene(scene);
                    stage.setTitle("Chat");
                    Image icono = new Image("icono.png");
                    stage.getIcons().add(icono);

                    stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent t) {
                            Platform.exit();
                            System.exit(0);
                        }
                    });

                    //stage.setResizable(true);
                    stage.show();

                    cerrarVentanaActual();
                } catch (IOException ioe) {
                    ExceptionHandler.manejarError("Error al iniciar sesión",ioe);
                }
            } else {
                alert.setVisible(true);
                tfContrasenia.setText("");
            }

        } else {
            if (!validacionNombre(tfName)) {
                ExceptionHandler.mostrarErrorAlUsuario("El nombre no puede contener el caracter \":\"");
            } else {
                ExceptionHandler.mostrarErrorAlUsuario("Campos vacíos o incorrectos");
            }
        }
    }

    @FXML
    private void crearCuenta() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/vista/registroVista.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Scene scene2 = new Scene(root1);
            scene2.getStylesheets().add(getClass().getResource("/com/example/vista/estilos.css").toExternalForm());

            Stage stage = new Stage();
            stage.setScene(scene2);
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(primaryStage);
            stage.setScene(scene2);
            stage.setTitle("Registro");
            Image icono = new Image("icono.png");
            stage.getIcons().add(icono);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            ExceptionHandler.manejarError("Error al registrarse", e);
        }

    }

    private void cerrarVentanaActual() {
        if (primaryStage != null) {
            primaryStage.close();
        } else {
            System.out.println("El primaryStage es nulo.");
        }
    }



    private boolean validacionNombre(TextField tfName) {
        String nombre = tfName.getText();
        if (nombre.contains(":")) {
            return false;
        } else {
            return true;
        }
    }
}







