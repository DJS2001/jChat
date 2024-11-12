package com.example.controlador;

import com.example.modelo.SQLiteManager;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.List;

public class RegistroControlador {
    SQLiteManager manager = new SQLiteManager();
    @FXML
    private TextField tfRegUsuario;
    @FXML
    private TextField tfRegContrasenia;
    @FXML
    private TextField tfConfirmContrasenia;
    @FXML
    private Button btnRegistro;

    @FXML
    public void registrarUsuario() {
        String usuario = tfRegUsuario.getText();
        String contrasenia = tfRegContrasenia.getText();
        String confirmContrasenia = tfConfirmContrasenia.getText();

        if (validarUsuario(usuario) && validaContrasenia(contrasenia) && validaContrasenia(confirmContrasenia)
            && passWEquals(contrasenia,confirmContrasenia)) {
            manager.crearUsuario(usuario,contrasenia);

            Stage stage = (Stage) btnRegistro.getScene().getWindow();
            stage.close();
        } else {
            tfRegUsuario.setText("");
            tfRegContrasenia.setText("");
            tfConfirmContrasenia.setText("");
        }

    }

    public boolean validarUsuario(String usuario) {
        if (usuario.isEmpty()) {
            ExceptionHandler.mostrarErrorAlUsuario("Campo usuario en blanco");
            return false;
        }

        if (validaSpcCaracteres(usuario)) {
            ExceptionHandler.mostrarErrorAlUsuario("El usuario no puede contener caracteres especiales");
            return false;
        }

        if (validaEspacios(usuario)) {
            ExceptionHandler.mostrarErrorAlUsuario("El usuario no puede contener espacios");
            return false;
        }

        List<String> listaUsuarios = manager.obtenerUsuarios();

        for (String u : listaUsuarios) {
            if (u.equals(usuario)) {
                ExceptionHandler.mostrarErrorAlUsuario("Usuario no disponible");
                return false;
            }
        }

        return true;
    }

    public boolean validaContrasenia(String contrasenia) {
        if (contrasenia.isEmpty()) {
            ExceptionHandler.mostrarErrorAlUsuario("Campo contraseña en blanco");
            return false;
        }

        if (validaSpcCaracteres(contrasenia)) {
            ExceptionHandler.mostrarErrorAlUsuario("La contraseña no puede contener caracteres especiales");
            return false;
        }

        if (validaEspacios(contrasenia)) {
            ExceptionHandler.mostrarErrorAlUsuario("La contraseña no puede contener espacios");
            return false;
        }

        return true;
    }

    public boolean validaEspacios(String texto) {
        if (texto.contains(" ")) {return true;}
        return false;
    }

    public boolean validaSpcCaracteres(String texto) {
        if (texto.contains("[a-zA-Z@._]+")) {return true;}
        return false;
    }

    public boolean passWEquals(String contrasenia, String contrasenia2) {
        if (!contrasenia.equals(contrasenia2)) {
            ExceptionHandler.mostrarErrorAlUsuario("Las contraseñas no coinciden");
            return false;
        }
        return true;
    }
}

