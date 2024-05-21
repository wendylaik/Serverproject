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
    private static final Verfication fileUserAuthentication = new Verfication(); // Objeto para

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT, 0, InetAddress.getLocalHost())) {
            // Crear un ServerSocket y mostrar mensajes informativos
            System.out.println("Servidor escuchando en localhost en el puerto " + PORT);
            System.out.println("Dirección IP del servidor: " + InetAddress.getLocalHost().getHostAddress());

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
            try (
                    PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true); BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));) {
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    System.out.println("Mensaje del cliente: " + inputLine);
                    String[] AuthParts = inputLine.split("-");
                    System.out.println(AuthParts);
                    if (!Boolean.parseBoolean(AuthParts[1])) {
                        handleAuthenticationRequest(inputLine, out);
                        continue;

                    } // Manejar solicitudes de autenticación
                    else {

                        // Saltar al siguiente ciclo de bucle después de manejar la autenticación
                    }

                    // Si el usuario está autenticado, manejar otras solicitudes
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

        private void handleAuthenticationRequest(String inputLine, PrintWriter out) throws IOException {
            // Verificar si la solicitud comienza con "Login_"
            // Separar los datos recibidos por el cliente (código de operación, nombre de usuario y contraseña)
            String[] parts = inputLine.split(",");
            int operationCode = Integer.parseInt(parts[0]);
            String username = parts[1];
            String password = parts[2];

            // Realizar la autenticación o el registro según el código de operación recibido
            switch (operationCode) {
                case 0:
                    // Autenticación del usuario
                    boolean isAuthenticated = fileUserAuthentication.authenticateUser(username, password);
                    if (isAuthenticated) {
                        out.println("auth exitoso true"); // Enviar el resultado de la autenticación al cliente
                        isAuthenticated = true; // Marcar al usuario como autenticado
                    } else {
                        out.println("auth exitoso false"); // Enviar un mensaje indicando que la autenticación falló
                        // Cerrar el socket y finalizar el hilo
                        clientSocket.close();
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
                case 2:
                    // Obtener roles del usuario
                    List<String> userRoles = fileUserAuthentication.getUserRoles(username);
                    out.println(String.join(",", userRoles));
                    break;
                default:
                    System.err.println("Código de operación no válido");
                    break;
            }

        }

    }
}
