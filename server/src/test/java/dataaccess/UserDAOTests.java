package dataaccess;
import dataaccess.DataAccessException;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.*;

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
            users.createUser(new UserData("get-user", "pwd", "email"));
            actual = users.getUser("get-user");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        var expected = new UserData("get-user", "pwd", "email");
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testGetUserFailure() {
        reset();
        var users = new SQLUserDAO();
        Assertions.assertThrows(NullPointerException.class, () -> {
            users.createUser(new UserData("get-user", "pwd", "email"));
            var res = users.getUser("name not in db").username();
        });
    }

    @Test
    public void testCreateUserSuccess() {
        reset();
        var users = new SQLUserDAO();
        var actual = new UserData("no", "no", "no");
        try {
            users.createUser(new UserData("create-user", "pwd", "email"));
            actual = users.getUser("create-user");
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        var expected = new UserData("create-user", "pwd", "email");
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testCreateUserFailure() {
        reset();
        var users = new SQLUserDAO();
        Assertions.assertThrows(NullPointerException.class, () -> {
            users.createUser(new UserData("create-user", "pwd", "email"));
            var res = users.getUser("name not in db").username();
        });
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
