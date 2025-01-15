import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.util.Base64;

public class KeyGeneratorUtil {

    /**
     * Generates a secure key for JWT using the HS256 algorithm.
     *
     * @return A Base64 encoded key.
     */
    public static String generateSecretKey() {
        // Generate a secure key for HS256
        SecretKey key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // Return the Base64 encoded key
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static void main(String[] args) {
        try {
            // Generate and display the secure key
            String secretKey = generateSecretKey();
            System.out.println("Generated Secret Key: " + secretKey);
            System.out.println("Copy this key to your configuration file for JWT usage.");
        } catch (Exception e) {
            System.err.println("Error generating secret key: " + e.getMessage());
        }
    }
}
