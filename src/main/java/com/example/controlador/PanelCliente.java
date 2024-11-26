package com.example.controlador;

import com.example.modelo.UsuarioConectado;
import com.example.modelo.UsuarioConectadoSingleton;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.Socket;

public class PanelCliente {
    @FXML
    private TextField tfMensaje;
    @FXML
    private TextArea taMensajes;
    @FXML
    private ComboBox<UsuarioConectado> cbNewChat;
    private HiloEnviar hiloEnviar;
    private HiloRecibir hiloRecibir;
    private Servidor servidor;

    @FXML
    private void initialize() {
        // Obtener la lista de usuarios conectados del Singleton
        ObservableList<UsuarioConectado> usuarios = UsuarioConectadoSingleton.getInstance().getUsuariosConectados();
        System.out.println(usuarios.size());
        // Configurar el ComboBox con la lista de usuarios conectados
        cbNewChat.setItems(usuarios);

        // Agregar un listener para detectar cambios en la lista de usuarios conectados
        usuarios.addListener((ListChangeListener<UsuarioConectado>) change -> {
            Platform.runLater(() -> {
                // Cada vez que cambie la lista (un nuevo usuario se agrega, etc.)
                // Actualizamos el ComboBox con la lista actualizada
                cbNewChat.setItems(usuarios);
            });
        });
    }

    @FXML
    private void enviarMensaje() {
        String texto = tfMensaje.getText();
        if (!texto.isBlank()) {
            hiloEnviar.mandarMensaje(texto);
            tfMensaje.clear();
            taMensajes.positionCaret(texto.length());
        }
    }

    public void inicializarHilos(String nombre, Socket socket) {
        this.hiloRecibir = new HiloRecibir(socket, nombre, taMensajes);
        this.hiloEnviar = new HiloEnviar(socket, nombre);
        hiloRecibir.esperarMensaje();
        hiloEnviar.mensajeBienvenida();
    }
}
