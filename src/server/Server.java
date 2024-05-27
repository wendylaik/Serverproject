package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Arrays;


public class Server {

    private static final int PORT = 12345;
    private static final String CONTENT_DIR = "server_content";

    public static void main(String[] args) {
    try {
        // Cambiar la dirección IP del servidor para la dirección IP de Hamachi
        InetAddress inetAddress = InetAddress.getByName("192.168.100.7"); // Reemplaza "tu_direccion_hamachi" con tu dirección IP de Hamachi
        try (ServerSocket serverSocket = new ServerSocket(PORT, 50, inetAddress)) {
            System.out.println("Servidor iniciado en el puerto " + PORT);
            System.out.println("Dirección IP del servidor: " + inetAddress.getHostAddress());

            while (true) {
                Socket clientSocket = serverSocket.accept();
                System.out.println("Nuevo cliente conectado: " + clientSocket.getInetAddress().getHostAddress());
                new Thread(new ClientHandler(clientSocket)).start();
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
}

    static class ClientHandler implements Runnable {

        private Socket clientSocket;

        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
        }

        @Override
        public void run() {
            try (InputStream input = clientSocket.getInputStream(); OutputStream output = clientSocket.getOutputStream(); BufferedReader reader = new BufferedReader(new InputStreamReader(input)); PrintWriter writer = new PrintWriter(output, true)) {

                String request;
                while ((request = reader.readLine()) != null) {
                    handleRequest(request, writer, output);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    clientSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

 private void handleRequest(String request, PrintWriter writer, OutputStream output) {
    String[] requestParts = request.split(",");
    String command = requestParts[0];
    String fileType = requestParts.length > 1 ? requestParts[1] : "";

    String directoryPath = CONTENT_DIR + File.separator + fileType;

    switch (command) {
        case "LOAD":
            File dir = new File(directoryPath);
            if (dir.exists() && dir.isDirectory()) {
                String[] files = dir.list();
                if (files != null) {
                    for (String file : files) {
                        writer.println(file);
                    }
                }
                writer.println(""); // End of list indicator
            } else {
                writer.println("DIRECTORY NOT FOUND");
            }
            break;
            case "UPLOAD":
            if (requestParts.length > 2) {
                String fileName = requestParts[2];
                File file = new File(directoryPath, fileName);
                try (FileOutputStream fos = new FileOutputStream(file)) {
                    writer.println("200 OK");
                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = clientSocket.getInputStream().read(buffer)) != -1) {
                        fos.write(buffer, 0, bytesRead);
                    }
                    fos.flush();
                    System.out.println("Archivo recibido y guardado: " + file.getAbsolutePath());
                } catch (IOException e) {
                    e.printStackTrace();
                    writer.println("500 ERROR");
                }
            }
            break;
        case "DOWNLOAD":    
            if (requestParts.length > 2) {
                String fileName = requestParts[2];
                File file = new File(directoryPath, fileName);
                if (file.exists() && !file.isDirectory()) {
                    writer.println("200 OK");
                    writer.println(file.length()); // Enviar el tamaño del archivo
                    try (FileInputStream fis = new FileInputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = fis.read(buffer)) != -1) {
                            output.write(buffer, 0, bytesRead);
                        }
                        output.flush();
                        writer.println(""); // Indicador de fin de transmisión
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    writer.println("404 Not Found");
                }
            }
            break;
       case "REGISTER":
            if (requestParts.length > 3) {
                String username = requestParts[1];
                String password = requestParts[2];
                String services = String.join(",", Arrays.copyOfRange(requestParts, 3, requestParts.length));
                System.out.println("Roles recibidos: " + services);  // Verificación de roles recibidos
                boolean registered = registerUser(username, password, services);
                writer.println("auth exitoso " + registered);
            }
            break;
        case "LOGIN":
            if (requestParts.length > 2) {
                String username = requestParts[1];
                String password = requestParts[2];
                boolean authenticated = authenticateUser(username, password);
                if (authenticated) {
                    String roles = getUserRoles(username);
                    writer.println("auth exitoso true " + roles);
                } else {
                    writer.println("auth exitoso false");
                }
            }
            break;
        default:
            writer.println("UNKNOWN COMMAND");
            break;
    }
}

private String getUserRoles(String username) {
    String rutaArchivo = "Users.txt";
    StringBuilder roles = new StringBuilder();
    try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            String[] partes = linea.split(",");
            if (partes.length > 2 && partes[0].equals(username)) {
                for (int i = 2; i < partes.length; i++) {
                    roles.append(partes[i].trim()).append(",");
                }
                if (roles.length() > 0) {
                    roles.setLength(roles.length() - 1); // Eliminar la última coma
                }
                break;
            }
        }
    } catch (IOException e) {
        e.printStackTrace();
    }
    return roles.toString();
}

private boolean registerUser(String username, String password, String services) {
    String hashedPassword = PasswordUtils.hashPassword(password);
    String userRecord = username + "," + hashedPassword + "," + services;
    System.out.println("Registro de usuario: " + userRecord);  // Verificación de registro de usuario

    try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Users.txt", true)))) {
        out.println(userRecord);
        return true;
    } catch (IOException e) {
        e.printStackTrace();
        return false;
    }
}

        private boolean authenticateUser(String username, String password) {
            String rutaArchivo = "Users.txt";
            try (BufferedReader br = new BufferedReader(new FileReader(rutaArchivo))) {
                String linea;
                while ((linea = br.readLine()) != null) {
                    String[] partes = linea.split(",");
                    if (partes.length > 2 && partes[0].equals(username)) {
                        String storedPassword = partes[1];
                        return PasswordUtils.verifyPassword(password, storedPassword);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }
    }
}