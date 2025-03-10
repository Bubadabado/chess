package dataaccess;
import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.AdminService;

public class AuthDAOTests {
    private void reset() {
        AdminService.clear();
    }

    @Test
    public void testCreateAuthSuccess() {
        reset();
        var auths = new SQLAuthDAO();
        var expected = "super secure auth";
        var actual = "something else";
        try {
            auths.createAuth(new AuthData(expected, "create-user"));
            actual = auths.getAuth(expected).authToken();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testCreateAuthFailure() {
        reset();
        var auths = new SQLAuthDAO();
        Assertions.assertThrows(DataAccessException.class, () -> {
            auths.createAuth(new AuthData(null, null));
        });
    }

    @Test
    public void testGetAuthSuccess() {
        reset();
        var auths = new SQLAuthDAO();
        var expected = "super secure auth";
        var actual = "something else";
        try {
            auths.createAuth(new AuthData(expected, "get-user"));
            actual = auths.getAuth(expected).authToken();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testGetAuthFailure() {
        reset();
        var auths = new SQLAuthDAO();
        Assertions.assertThrows(NullPointerException.class, () -> {
            auths.getAuth("literally anything").authToken();
        });
    }
}
