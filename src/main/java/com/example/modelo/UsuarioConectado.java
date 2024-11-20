package com.example.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.net.Socket;

public class UsuarioConectado {
    private String nombre;
    private Socket socket;
    private static ObservableList<UsuarioConectado> usuariosConectados = FXCollections.observableArrayList();

    public UsuarioConectado(String nombre, Socket socket) {
        this.nombre = nombre;
        this.socket = socket;
    }

    public UsuarioConectado() {}

    public Socket getSocket() {return socket;}

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return nombre;
    }

    public static ObservableList<UsuarioConectado> getListaUsuarios() {return usuariosConectados;}

    public void addUsuario() {
        usuariosConectados.add(this);
    }

}
