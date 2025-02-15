package service;
import java.util.UUID;

public class AuthGen {
    public static String generateAuthToken() {
        return UUID.randomUUID().toString();
    }
}
