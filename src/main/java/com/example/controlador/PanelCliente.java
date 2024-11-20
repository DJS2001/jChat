package com.example.controlador;

import com.example.modelo.UsuarioConectado;
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
    private ComboBox cbNewChat;

    private HiloEnviar hiloEnviar;
    private HiloRecibir hiloRecibir;

    @FXML
    private void initialize() {
        ObservableList<UsuarioConectado> ol = UsuarioConectado.getListaUsuarios();
        cbNewChat.setItems(ol);

        // Agregar un listener para asegurarte de que el ComboBox se actualice si la lista cambia.
        ol.addListener((ListChangeListener<UsuarioConectado>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    System.out.println("Usuario agregado: " + change.getAddedSubList());
                }
            }
        });

        System.out.println("Usuarios conectados iniciales:" + ol.size());
        for (UsuarioConectado u : ol) {
            System.out.println("Nombre: " + u.getNombre());
            System.out.println("Socket: " + u.getSocket());
        }
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

    public void aniadirCliente(String nombre, Socket socket) {
        UsuarioConectado usuarioConectado = new UsuarioConectado(nombre, socket);
        usuarioConectado.addUsuario();

        System.out.println("*aniadirCliente* nombre: " + usuarioConectado.getNombre());
        System.out.println("Lista actualizada en aniadirCliente:");

        for (UsuarioConectado u : UsuarioConectado.getListaUsuarios()) {
            System.out.println("Nombre:" + u.getNombre());
        }

        cbNewChat.setItems(UsuarioConectado.getListaUsuarios());
    }

}