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
            auths.createAuth(new AuthData(expected, "user"));
            actual = auths.getAuth(expected).authToken();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expected, actual);
    }
}
