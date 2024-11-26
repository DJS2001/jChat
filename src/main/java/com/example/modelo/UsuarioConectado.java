package com.example.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.Socket;

public class UsuarioConectado {
    private String nombre;
    private Socket socket;

    public UsuarioConectado(String nombre, Socket socket) {
        this.nombre = nombre;
        this.socket = socket;
    }

    public Socket getSocket() {return socket;}

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }
}
