import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
public class GenPassword {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hash = encoder.encode("admin123");
        System.out.println("Hash: " + hash);
        System.out.println("Matches: " + encoder.matches("admin123", hash));
    }
}