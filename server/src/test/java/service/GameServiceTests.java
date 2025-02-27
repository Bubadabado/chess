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
            var id = GameService.createGame(new CreateGameRequest("invalid auth", "new game")).gameID();
        });
    }

    @Test
    public void testListGamesSuccess() {
        reset();
        var actual = "";
        var expected = "ListGameResult[games=[GameData[gameID=1, whiteUsername=null, blackUsername=null, gameName=new game]]]";
        try {
            var regRes = UserService.register(new RegisterRequest("test", "pwd", "email"));
            GameService.createGame(new CreateGameRequest(regRes.authToken(), "new game"));
            actual = GameService.listGames(new ListGameRequest(regRes.authToken())).toString();
        } catch (DataAccessException _) {}
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testListGamesFailure() {
        reset();
        Assertions.assertThrows(DataAccessException.class, () -> {
            var regRes = UserService.register(new RegisterRequest("test", "pwd", "email"));
            GameService.createGame(new CreateGameRequest(regRes.authToken(), "new game"));
            GameService.listGames(new ListGameRequest("invalid auth"));
        });
    }

    @Test
    public void testJoinGameSuccess() {
        reset();
        Assertions.assertDoesNotThrow(() -> {
            var regRes = UserService.register(new RegisterRequest("test", "pwd", "email"));
            GameService.createGame(new CreateGameRequest(regRes.authToken(), "new game"));
            GameService.joinGame(new JoinGameRequest(regRes.authToken(), "black", 1));
        });
    }
    @Test
    public void testJoinGameFailure() {
        reset();
        Assertions.assertThrows(DataAccessException.class, () -> {
            var regRes = UserService.register(new RegisterRequest("test", "pwd", "email"));
            GameService.createGame(new CreateGameRequest(regRes.authToken(), "new game"));
            GameService.joinGame(new JoinGameRequest("invalid auth", "black", 1));
        });
    }
}
