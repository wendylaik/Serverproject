package server;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class RegisterHandler {

    public static boolean registerUser(String username, String password, String[] services) {
        String hashedPassword = PasswordUtils.hashPassword(password);

        StringBuilder servicesBuilder = new StringBuilder();
        for (String service : services) {
            servicesBuilder.append(service).append(",");
        }
        String servicesString = servicesBuilder.toString();
        if (servicesString.endsWith(",")) {
            servicesString = servicesString.substring(0, servicesString.length() - 1); // Eliminar la última coma
        }

        String userRecord = username + "," + hashedPassword + "," + servicesString;

        try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("Users.txt", true)))) {
            out.println(userRecord);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
