package com.example.modelo;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SQLiteManager {

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.err.println("No se pudo registrar el driver de SQLite.");
            e.printStackTrace();
        }
    }

    private static final String DB_URL = "jdbc:sqlite:chat.db";

    public void crearTablaMensajes() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS mensajes (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "usuario TEXT," +
                    "mensaje TEXT," +
                    "fecha TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                    "idChat INTEGER," +
                    "FOREIGN KEY (idChat) REFERENCES chats(id))";

            stmt.execute(sql);
        } catch (SQLException e) {
            System.err.println("Error al crear la tabla de mensajes: " + e.getMessage());
        }
    }


    public void crearTablaUsuarios() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS usuarios (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "usuario TEXT," +
                    "contrasenia TEXT)";

            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al crear la tabla de usuarios: " + e.getMessage());
        }
    }


    public void crearTablaChats() {
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            String sql = "CREATE TABLE IF NOT EXISTS chats (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT)";

            stmt.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error al crear la tabla de usuarios: " + e.getMessage());
        }
    }

    public void insertarMensaje(String sender, String message) {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String sql = "INSERT INTO mensajes (usuario, mensaje, fecha) VALUES (?, ?, datetime('now'))";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, sender);
                statement.setString(2, message);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar mensaje " + message + " " + e.getMessage());
        }
    }

    public void agregarChat() {
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement stmt = connection.createStatement()) {
            String sql = "INSERT INTO chats DEFAULT VALUES";
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            System.err.println("Error al agregar un nuevo chat: " + e.getMessage());
        }
    }

    public void crearUsuario(String usuario, String contrasenia) {
        try (Connection connection = DriverManager.getConnection(DB_URL)){
            String sql = "INSERT INTO usuarios (usuario, contrasenia) VALUES (?,?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1,usuario);
                statement.setString(2,contrasenia);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            System.err.println("Error al insertar usuario " + usuario + " " + e.getMessage());
        }

    }

    /*public List<String> obtenerMensajes(String idChat) {
        List<String> mensajes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT mensaje FROM mensajes WHERE idChat = ? ORDER BY fecha";

            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, idChat);

                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        String mensaje = rs.getString("mensaje");
                        mensajes.add(mensaje);
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("No se pudieron obtener los mensajes: " + e.getMessage());
        }

        return mensajes;  // Retornar la lista de mensajes
    }*/

    public List<String> obtenerMensajes() {
        List<String> mensajes = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT mensaje FROM mensajes ORDER BY fecha")) {

            while (resultSet.next()) {
                String mensaje = resultSet.getString("mensaje");

                mensajes.add(mensaje);
            }
        } catch (SQLException e) {
            System.err.println("No se pudieron obtener los mensasjes: " + e.getMessage());
        }
        return mensajes;
    }


    public List<String> obtenerUsuarios() {
        List<String> usuarios = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT usuario FROM usuarios")){

            while (resultSet.next()) {
                String usuario = resultSet.getString("usuario");
                usuarios.add(usuario);
            }

        } catch (SQLException e) {
            System.err.println("No se pudieron obtener los usuarios: " + e.getMessage());
        }
        return usuarios;
    }


    public boolean validaCuenta(String usuario, String contrasenia) {
        try (Connection connection = DriverManager.getConnection(DB_URL)){
            String sql = "SELECT usuario, contrasenia FROM usuarios WHERE ? LIKE usuario AND ? LIKE contrasenia";
            try (PreparedStatement statement = connection.prepareStatement(sql)){
                statement.setString(1,usuario);
                statement.setString(2,contrasenia);

                ResultSet resultSet = statement.executeQuery();

                String resulName =  resultSet.getString(1);
                String resulPassW = resultSet.getString(2);

                if (resulName != null && resulPassW != null) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("No se pudo validar la cuenta: " + e.getMessage());
        }

        return false;
    }

    public boolean chGeneralExiste() {
        try (Connection connection = DriverManager.getConnection(DB_URL)) {
            String sql = "SELECT id FROM chats WHERE id = 1";

            try (Statement stmt = connection.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            System.err.println("No se pudo comprobar si el chat general existe: " + e.getMessage());
        }

        return false;
    }


    public static void main(String[] args) {

    }
}
