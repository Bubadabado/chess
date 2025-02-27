package service;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

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
        } catch (DataAccessException e) {}
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
        reset();
        var actual = "";
        var expected = "test";
        try {
            UserService.register(new RegisterRequest("test", "pwd", "email"));
            actual = UserService.login(new LoginRequest("test", "pwd")).username();
        } catch (DataAccessException e) {}
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testLoginFailure() {
        reset();
        Assertions.assertThrows(DataAccessException.class, () -> {
            UserService.register(new RegisterRequest("test", "pwd", "email"));
            UserService.login(new LoginRequest("test", "wrong_pwd")).username();
        });
    }

    @Test
    public void testLogoutSuccess() {
        reset();
        Assertions.assertDoesNotThrow(() -> {
            var regreq = UserService.register(new RegisterRequest("test", "pwd", "email"));
            UserService.logout(new LogoutRequest(regreq.authToken()));
        });
    }
    @Test
    public void testLogoutFailure() {
        Assertions.assertThrows(DataAccessException.class, () -> {
            UserService.register(new RegisterRequest("test", "pwd", "email"));
            UserService.logout(new LogoutRequest("bad auth"));
        });
    }
}
