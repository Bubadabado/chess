package client;

import org.junit.jupiter.api.*;
import service.CreateGameRequest;
import server.Server;
import server.ServerFacade;
import service.*;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade("http://localhost:" + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void clearDB() throws Exception {
        facade.clear();
    }

    //register, login, logout, createGame, listGames, joinGame, observeGame
    @Test
    public void testRegisterSuccess() throws Exception {
        var res = facade.register(new RegisterRequest("un", "pw", "em"));
        assertEquals("un", res.username());
    }
    @Test
    public void testRegisterFailure() throws Exception {
        assertThrows(Exception.class, () -> {
            var res = facade.register(new RegisterRequest(null, "pw", "em"));
        });
    }

    @Test
    public void testLoginSuccess() throws Exception {
        var res = facade.register(new RegisterRequest("un", "pw", "em"));
        facade.logout(new LogoutRequest(res.authToken()));
        var res2 = facade.login(new LoginRequest("un", "pw"));
        assertEquals("un", res2.username());
    }
    @Test
    public void testLoginFailure() throws Exception {
        assertThrows(Exception.class, () -> {
            var res = facade.login(new LoginRequest("this user doesnt exist", "pwd"));
        });
    }

    @Test
    public void testLogoutSuccess() throws Exception {
        var res = facade.register(new RegisterRequest("un", "pw", "em"));
        assertDoesNotThrow(() -> {
            facade.logout(new LogoutRequest(res.authToken()));
        });
    }
    @Test
    public void testLogoutFailure() throws Exception {
        assertThrows(Exception.class, () -> {
            var res = facade.logout(new LogoutRequest("this auth doesnt exist"));
        });
    }

    @Test
    public void testCreateSuccess() throws Exception {
        var res = facade.register(new RegisterRequest("un", "pw", "em"));
        assertDoesNotThrow(() -> {
            facade.createGame(new service.CreateGameRequest(res.authToken(), "game1"));
        });
    }
    @Test
    public void testCreateFailure() throws Exception {
        assertThrows(Exception.class, () -> {
            var res = facade.createGame(new service.CreateGameRequest("no auth", "game1"));
        });
    }

    @Test
    public void testListSuccess() throws Exception {
        var res = facade.register(new RegisterRequest("un", "pw", "em"));
        assertDoesNotThrow(() -> {
            facade.listGames(new ListGameRequest(res.authToken()));
        });
    }
    @Test
    public void testListFailure() throws Exception {
        assertThrows(Exception.class, () -> {
            var res = facade.listGames(new ListGameRequest("no auth"));
        });
    }

    @Test
    public void testJoinSuccess() throws Exception {
        var res = facade.register(new RegisterRequest("un", "pw", "em"));
        assertDoesNotThrow(() -> {
            var game = facade.createGame(new service.CreateGameRequest(res.authToken(), "game1"));
            facade.joinGame(new JoinGameRequest(res.authToken(), "BLACK", game.gameID()));
        });
    }
    @Test
    public void testJoinFailure() throws Exception {
        var res = facade.register(new RegisterRequest("un", "pw", "em"));
        assertThrows(Exception.class, () -> {
            facade.joinGame(new JoinGameRequest(res.authToken(), "BLACK", 0));
        });
    }

    @Test
    public void testObserveSuccess() throws Exception {
        var res = facade.register(new RegisterRequest("un", "pw", "em"));
        assertDoesNotThrow(() -> {
            var game = facade.createGame(new CreateGameRequest(res.authToken(), "game1"));
            facade.observeGame(new JoinGameRequest(res.authToken(), null, 0));
        });
    }
    @Test
    public void testObserveFailure() throws Exception {
        var res = facade.register(new RegisterRequest("un", "pw", "em"));
        assertThrows(Exception.class, () -> {
            facade.observeGame(new JoinGameRequest(res.authToken(), "BLACK", 0));
        });
    }
}
