package dataaccess;
import dataaccess.DataAccessException;
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
    }
    @Test
    public void testCreateGameFailure() {
        reset();
        var games = new SQLGameDAO();
    }

    @Test
    public void testFindGameSuccess() {
        reset();
        var games = new SQLGameDAO();
    }
    @Test
    public void testFindGameFailure() {
        reset();
        var games = new SQLGameDAO();
    }

    @Test
    public void testJoinGameSuccess() {
        reset();
        var games = new SQLGameDAO();
    }
    @Test
    public void testJoinGameFailure() {
        reset();
        var games = new SQLGameDAO();
    }

    @Test
    public void testGetColorSuccess() {
        reset();
        var games = new SQLGameDAO();
    }
    @Test
    public void testGetColorFailure() {
        reset();
        var games = new SQLGameDAO();
    }

    @Test
    public void testListGamesSuccess() {
        reset();
        var games = new SQLGameDAO();
    }
    @Test
    public void testListGamesFailure() {
        reset();
        var games = new SQLGameDAO();
    }

    @Test
    public void testClearGamesSuccess() {
        reset();
        var games = new SQLGameDAO();
        Assertions.assertDoesNotThrow(games::clearGames);
    }
}
