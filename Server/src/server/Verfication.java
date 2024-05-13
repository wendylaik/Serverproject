
package server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 *
 * @author Jordy vindas
 */
public class Verfication {
     private static final String FILE_PATH = "Users.txt";
    
    // Genera una sal (salt) aleatoria
    public boolean authenticateUser(String username, String password) {
    try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
        String line;
        while ((line = reader.readLine()) != null) {
            // Divide la línea en usuario y contraseña usando un delimitador (por ejemplo, coma)
            String[] parts = line.split(",");
            if (parts.length >= 2) {
                String storedUsername = parts[0];
                String storedPassword = parts[1];

                // Verifica si el usuario y la contraseña coinciden
                if (username.equals(storedUsername) && password.equals(storedPassword)) {
                    return true; // Autenticación exitosa
                }
            } else {
                System.err.println("La línea no contiene un usuario y una contraseña válidos: " + line);
            }
        }
    } catch (IOException e) {
        System.err.println("Error al leer el archivo de usuarios: " + e.getMessage());
    }
    return false; // Autenticación fallida
}

    // Método para registrar un nuevo usuario
    public void registerUser(String username, String password) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // Escribe el nuevo usuario en el archivo
            writer.write(username + "," + password);
            writer.newLine();
            System.out.println("Usuario registrado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de usuarios: " + e.getMessage());
        }
    }
}

