package com.example.controlador;

import com.example.modelo.SQLiteManager;
import com.example.modelo.UsuarioConectado;
import javafx.collections.ObservableList;

import java.io.*;
import java.net.Socket;

public class ComunicadorCliente extends Thread {
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;
    private SQLiteManager sqliteManager;
    private Servidor servidor;
    private ObservableList<UsuarioConectado> usuariosConectados;

    public ComunicadorCliente(Socket socket, Servidor servidor, ObservableList<UsuarioConectado> usuariosConectados) {
        try {
            this.socket = socket;
            this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.sqliteManager = new SQLiteManager();
            this.servidor = servidor;
        } catch (IOException ioe) {
            ExceptionHandler.manejarError("Error al inicializar el comunicador del cliente", ioe);
        }
    }


    @Override
    public void run() {
        try {
            //Estas lineas comentadas dan error:
            //UsuarioConectado uc = new UsuarioConectado("",socket);
            //usuariosConectados.add(uc);

            String mensaje = null;

            while (socket.isConnected()) {
                mensaje = br.readLine();
                this.servidor.enviarMensaje(mensaje);
            }
        } catch (IOException ioe) {
            //System.out.println("Cliente desconectado");
        } finally {
            cerrarSocket();
        }
    }


    public void cerrarSocket() {
        try {
            if (!socket.isClosed()) {
                System.out.println("Cerrando el socket");
                servidor.cerrarSocket(socket);
                if (br != null) br.close();
                if (bw != null) bw.close();
                socket.close();
            }
        } catch (IOException e) {
            ExceptionHandler.manejarError("Error al cerrar el socket", e);
        }
    }
}













