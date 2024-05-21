
package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

/**
 *
 * @author Jordy vindas
 */
public class Flujocliente {
    private final Socket socket;
    private final PrintWriter out;
    private final BufferedReader in;

    public Flujocliente(Socket socket, BufferedReader in, PrintWriter out) {
        this.socket = socket;
        this.out = out;
        this.in = in;
    }
       public void sendMessage(String message) {
        out.println(message);
    }

    // Método para recibir mensajes del servidor
   public String receiveMessage() throws IOException {
    String message = in.readLine();
    return (message != null) ? message : ""; // Devuelve una cadena vacía si el mensaje es null
}

    // Método para cerrar la conexión con el servidor
    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        } catch (IOException e) {
        }
    }
    
    
 }
