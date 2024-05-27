package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {

    private static final int PORT = 8080;
    private static final int MAX_THREADS = 8;
    private static final ExecutorService executor = Executors.newFixedThreadPool(MAX_THREADS);
    private static final Verfication fileUserAuthentication = new Verfication(); // Objeto para la autenticación

    public static void main(String[] args) {
        try {
            String IP = "25.65.94.55"; // Asegúrate de usar tu IP de Hamachi
            InetAddress hamachiAddress = InetAddress.getByName(IP);

            try (ServerSocket serverSocket = new ServerSocket(PORT, 0, hamachiAddress)) {
                // Mostrar mensajes informativos
                System.out.println("Servidor escuchando en " + IP + " en el puerto " + PORT);

                while (true) {
                    // Esperar y aceptar conexiones entrantes de clientes
                    Socket clientSocket = serverSocket.accept();
                    System.out.println("Cliente conectado desde " + clientSocket.getInetAddress());

                    // Crear un nuevo hilo (ClientHandler) para manejar las solicitudes del cliente
                    Runnable clientHandler = new ClientHandler(clientSocket, fileUserAuthentication);
                    executor.execute(clientHandler); // Ejecutar el hilo en el ExecutorService
                }
            } catch (IOException e) {
                System.err.println("Error en el servidor: " + e.getMessage());
            }
        } catch (IOException e) {
            System.err.println("Error al obtener la dirección de Hamachi: " + e.getMessage());
        }
    }

    // Clase interna para manejar las solicitudes de un cliente específico
    static class ClientHandler implements Runnable {

        private final Socket clientSocket;
        private final Verfication fileUserAuthentication;

        public ClientHandler(Socket clientSocket, Verfication fileUserAuthentication) {
            this.clientSocket = clientSocket;
            this.fileUserAuthentication = fileUserAuthentication;
        }

        @Override
        public void run() {
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
                 BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Mensaje del cliente: " + inputLine);

                    // Verificar el tipo de mensaje recibido
                    if (inputLine.startsWith("getRoles,")) {
                        handleGetRolesRequest(inputLine, out);
                    } else {
                        handleAuthenticationOrRegistrationRequest(inputLine, out);
                    }
                }
            } catch (IOException e) {
                System.err.println("Error al manejar la conexión con el cliente: " + e.getMessage());
            } finally {
                try {
                    // Cerrar el socket del cliente
                    clientSocket.close();
                } catch (IOException e) {
                    System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
                }
            }
        }

        private void handleGetRolesRequest(String inputLine, PrintWriter out) {
            String[] parts = inputLine.split(",");
            if (parts.length != 2) {
                System.err.println("Formato de mensaje inválido: " + inputLine);
                out.println("error,Formato de mensaje inválido");
                return;
            }

            String username = parts[1];
            List<String> userRoles = fileUserAuthentication.getUserRoles(username);
            out.println(String.join(",", userRoles));
        }

        private void handleAuthenticationOrRegistrationRequest(String inputLine, PrintWriter out) {
            String[] parts = inputLine.split(",");
            if (parts.length < 3) {
                System.err.println("Formato de mensaje inválido: " + inputLine);
                out.println("error,Formato de mensaje inválido");
                return;
            }

            int operationCode;
            try {
                operationCode = Integer.parseInt(parts[0]);
            } catch (NumberFormatException e) {
                System.err.println("Código de operación no válido: " + parts[0]);
                out.println("error,Código de operación no válido");
                return;
            }

            String username = parts[1];
            String password = parts[2];

            switch (operationCode) {
                case 0:
                    // Autenticación del usuario
                    boolean isAuthenticated = fileUserAuthentication.authenticateUser(username, password);
                    if (isAuthenticated) {
                        out.println("auth exitoso true"); // Enviar el resultado de la autenticación al cliente
                    } else {
                        out.println("auth exitoso false"); // Enviar un mensaje indicando que la autenticación falló
                        // Cerrar el socket y finalizar el hilo
                        try {
                            clientSocket.close();
                        } catch (IOException e) {
                            System.err.println("Error al cerrar el socket del cliente: " + e.getMessage());
                        }
                        return; // Salir del método run() para terminar el hilo
                    }
                    break;
                case 1:
                    // Registro de nuevo usuario
                    List<String> roles = new ArrayList<>();
                    if (parts.length > 3) {
                        roles = Arrays.asList(parts).subList(3, parts.length);
                    }
                    fileUserAuthentication.registerUser(username, password, roles);
                    out.println("auth exitoso true"); // Enviar un mensaje al cliente indicando que el registro fue exitoso
                    break;
                default:
                    System.err.println("Código de operación no válido");
                    out.println("error,Código de operación no válido");
                    break;
            }
        }
    }
} 