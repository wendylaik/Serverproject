package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class LoginHandler {
    private static final String USERS_FILE = "users.txt";

    public boolean authenticate(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(":");
                if (parts.length == 2 && parts[0].equals(username)) {
                    String hashedPassword = parts[1];
                    return PasswordUtils.verifyPassword(password, hashedPassword);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
