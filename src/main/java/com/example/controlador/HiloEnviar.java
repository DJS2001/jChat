package com.example.controlador;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;


public class HiloEnviar {
    private Socket socket;
    private String usuario;
    private BufferedWriter bw;

    public HiloEnviar(Socket socket, String usuario) {
        this.socket = socket;
        this.usuario = usuario;

        try {
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            System.err.println("Error E/S :" + e.getMessage());
        }
    }

    public void mandarMensaje(String texto) {
        try {
            bw.write(usuario + ": " + texto);
            bw.newLine();
            bw.flush();

        } catch (IOException ioe) {
            cierraStreams();
        }
    }

    public void mensajeBienvenida() {
        mandarMensaje("se ha unido a la sala.");
    }

    private void cierraStreams() {
        try {
            if (bw != null) bw.close();
            if (socket != null && !socket.isClosed()) socket.close();
        } catch (IOException e) {
            e.getMessage();
        }
    }
}















