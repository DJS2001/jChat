package com.example.modelo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class UsuarioConectadoSingleton {
    private static UsuarioConectadoSingleton instance;
    private ObservableList<UsuarioConectado> usuariosConectados;

    private UsuarioConectadoSingleton() {
        usuariosConectados = FXCollections.observableArrayList();
    }

    public static UsuarioConectadoSingleton getInstance() {
        if (instance == null) {
            instance = new UsuarioConectadoSingleton();
        }
        return instance;
    }

    public void addUsuario(UsuarioConectado usuario) {
        usuariosConectados.add(usuario);
    }

    public ObservableList<UsuarioConectado> getUsuariosConectados() {
        return usuariosConectados;
    }
}