package com.example.modelo;

import com.example.controlador.ExceptionHandler;
import com.example.controlador.MainControlador;
import com.example.controlador.Servidor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.util.concurrent.atomic.AtomicBoolean;

public class MainVentana extends Application {
    private static final int puerto = 51005;
    private static final AtomicBoolean servidorIniciado = new AtomicBoolean(false);
    private Servidor servidor;

    @Override
    public void start(Stage stage) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/vista/mainVista.fxml"));
        Parent root;
        try {
            root = fxmlLoader.load();
            Scene scene = new Scene(root, 320, 240);
            scene.getStylesheets().add(getClass().getResource("/com/example/vista/estilos.css").toExternalForm());
            Image icono = new Image("icono.png");
            stage.getIcons().add(icono);
            stage.setScene(scene);
            stage.setResizable(false);
            stage.show();

            synchronized (MainVentana.class) {
                if (!servidorIniciado.get()) {
                    servidorIniciado.set(true);

                    new Thread(() -> {
                        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
                            servidor = new Servidor(serverSocket);
                            servidor.iniciaServidor();
                        } catch (BindException be) {
                            //System.err.println("Direccion ya en uso");
                        } catch (IOException ioe) {
                            ExceptionHandler.manejarError("Error al iniciar el servidor", ioe);
                        }
                    }).start();
                }
            }



            MainControlador controller = fxmlLoader.getController();
            controller.setPrimaryStage(stage);
        } catch (IOException ioe) {
            ExceptionHandler.manejarError("Error al iniciar la ventana",ioe);
        }

    }

    @Override
    public void stop() {
        if (servidorIniciado.get()) {
            try {
                if (servidor != null) {
                    servidor.cerrarServidor();
                }
            } finally {
                servidorIniciado.set(false);
            }
        }
    }




    public static void main(String[] args) {
        launch();
    }
}
