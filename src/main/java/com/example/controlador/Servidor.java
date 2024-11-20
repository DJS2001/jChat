package com.example.controlador;

import com.example.modelo.SQLiteManager;
import com.example.modelo.UsuarioConectado;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
public class Servidor {

    private ServerSocket serverSocket;
    private static ConcurrentHashMap<String,Socket> listaSocket = new ConcurrentHashMap<String,Socket>();
    private SQLiteManager manager = new SQLiteManager();
    private BufferedReader br;
    private int contadorClientes = 0;

    public Servidor(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
        manager.crearTablaMensajes();
        manager.crearTablaUsuarios();
        manager.crearTablaChats();
    }

    public Servidor() {
    }

    public void iniciaServidor() {
        try {
            while (!serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();

                this.br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                System.out.println("Nuevo cliente conectado");

                synchronized (this) {
                    contadorClientes++;
                }

                ComunicadorCliente comunicadorCliente = new ComunicadorCliente(socket, this);
                Thread hilo = new Thread(comunicadorCliente);
                hilo.start();

                //recibirPrimerMensaje(socket);

                if (manager.chGeneralExiste()) {
                    enviarChatPrevio(socket);
                } else {
                    manager.agregarChat();
                }

            }
        } catch (SocketException se) {
            System.out.println("Cerrando el socket del ultimo cliente");
        } catch (IOException ioe) {
            ExceptionHandler.manejarError("Ha ocurrido un problema al conectar al cliente ", ioe);
        }
    }

    public synchronized void enviarMensaje(String mensaje) {
        try {
            manager.insertarMensaje(sacarUsuario(mensaje), mensaje);
            for (Socket socket : listaSocket.values()) {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                bw.write(mensaje);
                bw.newLine();
                bw.flush();
            }
        } catch (IOException e) {
            ExceptionHandler.manejarError("Error al enviar mensaje", e);
        }
    }

    public synchronized void cerrarSocket(Socket socket) {
        listaSocket.remove(socket);

        try {
            socket.close();
        } catch (IOException e) {
            ExceptionHandler.manejarError("Error al cerrar el socket", e);
        }

        synchronized (this) {
            contadorClientes--;
            if (contadorClientes <= 0) {
                cerrarServidor();
            }
        }
    }

    public synchronized void cerrarServidor() {
        try {
            System.out.println("No quedan clientes. Cerrando el servidor...");
            serverSocket.close();
        } catch (IOException e) {
            ExceptionHandler.manejarError("Error al cerrar el servidor", e);
        }
    }


    public void recibirPrimerMensaje(Socket socket) {
        String mensaje;

        try {
            mensaje = br.readLine();

            UsuarioConectado usuarioConectado = new UsuarioConectado(sacarUsuario(mensaje), socket);
            usuarioConectado.addUsuario();
            /*listaSocket.put(sacarUsuario(mensaje),socket);
            System.out.println("Tamaño lista en el servidor: " + listaSocket.size());

            ConcurrentHashMap<String, Socket> lista = Servidor.getListaSocket();
            String nombreUsuario = "";
            Socket socket2 = new Socket();

            for (Map.Entry<String, Socket> element : lista.entrySet()) {
                nombreUsuario = element.getKey();
                socket2 = element.getValue();
                System.out.println(nombreUsuario + " " + socket2);
            }*/
        } catch (IOException ioe) {
            ExceptionHandler.manejarError("No se ha podido enviar el mensaje", ioe);
        }

    }

    private String sacarUsuario(String mensaje) {
        return mensaje.split(":")[0];
    }

    private synchronized void enviarChatPrevio(Socket socket) throws IOException {
        List<String> mensajes = manager.obtenerMensajes("1");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        for (String mensaje : mensajes) {
            bw.write(mensaje);
            bw.newLine();
            bw.flush();
        }
    }

    public static ConcurrentHashMap<String, Socket> getListaSocket() {
        return listaSocket;
    }

}



