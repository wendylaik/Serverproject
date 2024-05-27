package server;

import java.util.List;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author Jordy vindas
 */

public class Verfication {

    private static final String FILE_PATH = "Users.txt";
    private static final String ROLES = "Roles.txt";
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
    public void registerUser(String username, String password, List<String> roles) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            // Escribe el nuevo usuario en el archivo
            writer.write(username + "," + password);
            writer.newLine();
            System.out.println("Usuario registrado con éxito.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de usuarios: " + e.getMessage());
        }

     // Guardar los roles del usuario
        try (BufferedWriter rolesWriter = new BufferedWriter(new FileWriter(ROLES, true))) {
            rolesWriter.write(username + "," + String.join(",", roles));
            rolesWriter.newLine();
            System.out.println("Roles del usuario guardados con éxito.");
        } catch (IOException e) {
            System.err.println("Error al escribir en el archivo de roles: " + e.getMessage());
        }
    }

    // Método para obtener los roles de un usuario
    public List<String> getUserRoles(String username) {
        List<String> roles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(ROLES))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length > 1) {
                    String storedUsername = parts[0];
                    if (storedUsername.equals(username)) {
                        roles.addAll(Arrays.asList(parts).subList(1, parts.length));
                        break;
                    }
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return roles;
    }
}
