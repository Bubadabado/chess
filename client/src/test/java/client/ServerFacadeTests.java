package client;

import org.junit.jupiter.api.*;
import server.Server;
import server.ServerFacade;
import service.LoginRequest;
import service.LogoutRequest;
import service.RegisterRequest;

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

}
