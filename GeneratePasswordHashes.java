import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeneratePasswordHashes {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String adminPassword = "admin123";
        String userPassword = "user123";
        
        String adminHash = encoder.encode(adminPassword);
        String userHash = encoder.encode(userPassword);
        
        System.out.println("Admin password hash: " + adminHash);
        System.out.println("User password hash: " + userHash);
        
        // Test to verify they work
        System.out.println("Admin password matches: " + encoder.matches(adminPassword, adminHash));
        System.out.println("User password matches: " + encoder.matches(userPassword, userHash));
    }
}
