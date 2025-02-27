package service;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {
    private void reset() {
        AdminService.clear();
    }

    @Test
    public void testRegisterSuccess() {
        reset();
        String expected = "test";
        String actual = "something else";
        try {
            actual = UserService.register(new RegisterRequest("test", "pwd", "email")).username();
        } catch (DataAccessException _) {}
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testRegisterFailure() {
        reset();
        Assertions.assertThrows(DataAccessException.class, () -> {
            UserService.register(new RegisterRequest("test", "pwd", "email"));
            UserService.register(new RegisterRequest("test", "pwd", "email"));
        });
    }

    @Test
    public void testLoginSuccess() {

    }
    @Test
    public void testLoginFailure() {

    }

    @Test
    public void testLogoutSuccess() {

    }
    @Test
    public void testLogoutFailure() {

    }
}
