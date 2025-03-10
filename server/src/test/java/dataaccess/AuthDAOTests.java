package dataaccess;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.AdminService;

public class AuthDAOTests {
    private void reset() {
        AdminService.clear();
    }
}
