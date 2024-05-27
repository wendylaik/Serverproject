package server;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class PasswordUtils {

    private static final String SALT = "1234"; // Sal fija solo para este ejemplo
    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;

    // Genera un hash de la contraseña usando PBKDF2
    public static String hashPassword(String password) {
        char[] chars = password.toCharArray();
        byte[] saltBytes = SALT.getBytes();

        PBEKeySpec spec = new PBEKeySpec(chars, saltBytes, ITERATIONS, KEY_LENGTH);
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = skf.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException(e);
        }
    }

    // Verifica que la contraseña proporcionada coincida con el hash almacenado
    public static boolean verifyPassword(String originalPassword, String storedPassword) {
        String newHash = hashPassword(originalPassword);
        return newHash.equals(storedPassword);
    }
}
