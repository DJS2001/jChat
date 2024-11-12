package com.example.controlador;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.Socket;


public class PanelCliente {
    @FXML
    private TextField tfMensaje;
    @FXML
    private TextArea taMensajes;
    private HiloEnviar hiloEnviar;
    private HiloRecibir hiloRecibir;


    @FXML
    private void initialize() {
        taMensajes.positionCaret(taMensajes.getText().length());
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
