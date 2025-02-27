package service;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

public class GameServiceTests {
    private void reset() {
        AdminService.clear();
    }

    @Test
    public void testCreateGameSuccess() {
        reset();
        int id = -1;
        try {
            var regRes = UserService.register(new RegisterRequest("test", "pwd", "email"));
            id = GameService.createGame(new CreateGameRequest(regRes.authToken(), "new game")).gameID();
        } catch (DataAccessException _) {}
        Assertions.assertEquals(1, id);
    }
    @Test
    public void testCreateGameFailure() {
        reset();
        Assertions.assertThrows(DataAccessException.class, () -> {
            var regRes = UserService.register(new RegisterRequest("test", "pwd", "email"));
            var id = GameService.createGame(new CreateGameRequest("", "new game")).gameID();
        });
    }

    @Test
    public void testListGamesSuccess() {

    }
    @Test
    public void testListGamesFailure() {

    }

    @Test
    public void testJoinGameSuccess() {

    }
    @Test
    public void testJoinGameFailure() {

    }
}
