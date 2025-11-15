import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHashTester {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String adminPass = encoder.encode("admin123");
        String userPass = encoder.encode("user123");
        
        System.out.println("Admin password hash: " + adminPass);
        System.out.println("User password hash: " + userPass);
    }
}
