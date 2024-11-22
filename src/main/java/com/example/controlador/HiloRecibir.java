package com.example.controlador;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

import java.io.*;
import java.net.Socket;

public class HiloRecibir {

    private Socket socket;
    private BufferedWriter bw;
    private BufferedReader br;
    private String nombre;
    private TextArea taMensajes;

    public HiloRecibir(Socket socket, String nombre, TextArea taMensajes) {
        try {
            this.socket = socket;
            this.nombre = nombre;
            this.taMensajes = taMensajes;
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException ioe) {
            ExceptionHandler.manejarError("Error al inicializar el cliente", ioe);
        }
    }


    public void esperarMensaje() {
        new Thread(() -> {
            while (socket.isConnected()) {
                try {
                    String mensajeGrupal = br.readLine();
                    if (mensajeGrupal != null) {
                        String finalMensajeGrupal = mensajeGrupal;
                        Platform.runLater(() -> {
                            taMensajes.appendText(finalMensajeGrupal + "\n");
                            taMensajes.positionCaret(taMensajes.getText().length());
                        });
                    }
                } catch (IOException ioe) {
                    cierraStreams();
                    break;
                }
            }
        }).start();
    }

    private void cierraStreams() {
        try {
            if (br != null) br.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

