package dataaccess;
import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.AdminService;

public class UserDAOTests {
    private void reset() {
        AdminService.clear();
    }

    @Test
    public void testGetUserSuccess() {
        reset();
        var users = new SQLUserDAO();
        var actual = new UserData("no", "no", "no");
        try {
            users.createUser(new UserData("user", "pwd", "email"));
            actual = users.getUser("user");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        var expected = new UserData("user", "pwd", "email");
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testGetUserFailure() {
        reset();
        var users = new SQLUserDAO();
        var actual = new UserData("no", "no", "no");
        try {
            users.createUser(new UserData("user", "pwd", "email"));
            actual = users.getUser("name not in db");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        String expected = null;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void testCreateUserSuccess() {
        //TODO
    }
    @Test
    public void testCreateUserFailure() {
        //TODO
    }

    @Test
    public void testClearUsersSuccess() {
        reset();
        var users = new SQLUserDAO();
        var actual = users.clearUsers();
        var expected = true;
        Assertions.assertEquals(expected, actual);
    }
}
