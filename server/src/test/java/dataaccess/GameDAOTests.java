package dataaccess;
import dataaccess.DataAccessException;
import model.AuthData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import service.AdminService;

public class GameDAOTests {
    private void reset() {
        AdminService.clear();
    }

    @Test
    public void testCreateGameSuccess() {
        reset();
        var games = new SQLGameDAO();
        Assertions.assertDoesNotThrow(() -> {
            games.createGame("new game");
        });
    }
    @Test
    public void testCreateGameFailure() {
        reset();
        var games = new SQLGameDAO();
        Assertions.assertThrows(DataAccessException.class, () -> {
            games.createGame(null);
        });
    }

    @Test
    public void testFindGameSuccess() {
        reset();
        var games = new SQLGameDAO();
        var expected = "name";
        var actual = "something else";
        try {
            int id = games.createGame("name");
            actual = games.findGame(id).gameName();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testFindGameFailure() {
        reset();
        var games = new SQLGameDAO();
        Assertions.assertThrows(NullPointerException.class, () -> {
            games.findGame(10000).gameID();
        });
    }

    @Test
    public void testJoinGameSuccess() {
        reset();
        var games = new SQLGameDAO();
        Assertions.assertDoesNotThrow(() -> {
            int id = games.createGame("new game");
            games.joinGame("user", "black", id);
        });
    }
    @Test
    public void testJoinGameFailure() {
        reset();
        var games = new SQLGameDAO();
        Assertions.assertThrows(DataAccessException.class, () -> {
            int id = games.createGame(null);
            games.joinGame("user", "not a real color", id);
        });
    }

    @Test
    public void testGetColorSuccess() {
        reset();
        var games = new SQLGameDAO();
        var expected = "user";
        var actual = "something else";
        try {
            int id = games.createGame("name");
            games.joinGame("user", "black", id);
            actual = games.getColor("black", id);
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testGetColorFailure() {
        reset();
        var games = new SQLGameDAO();
        Assertions.assertThrows(DataAccessException.class, () -> {
            int id = games.createGame(null);
            games.getColor("bad", id);
        });
    }

    @Test
    public void testListGamesSuccess() {
        reset();
        var games = new SQLGameDAO();
        int id = 0;
        var actual = "something else";
        try {
            id = games.createGame("name");
            actual = games.listGames().toString();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        var expected = "[GameData[gameID=" + id + ", whiteUsername=null, blackUsername=null, gameName=name]]";
        Assertions.assertEquals(expected, actual);
    }
    @Test
    public void testListGamesFailure() {
        reset();
        var games = new SQLGameDAO();
        Assertions.assertThrows(DataAccessException.class, () -> {
            int id = games.createGame(null);
            games.listGames();
        });
    }

    @Test
    public void testClearGamesSuccess() {
        reset();
        var games = new SQLGameDAO();
        Assertions.assertDoesNotThrow(games::clearGames);
    }
}
