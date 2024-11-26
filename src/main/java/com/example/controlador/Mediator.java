package com.example.controlador;

import java.io.IOException;
import java.net.ServerSocket;

public class Mediator {
    public static void main(String args[]) {
        int puerto = 51005;

        try (ServerSocket serverSocket = new ServerSocket(puerto)) {
            //Iniciamos el servidor. Al iniciar el servidor se inicia un hilo de ComunicadorCliente
            Servidor servidor = new Servidor(serverSocket);
            servidor.iniciaServidor();
        } catch (IOException ioe) {
            System.err.println("Ha ocurrido un problema al iniciar el servidor " + ioe.getMessage());
        }
    }
}