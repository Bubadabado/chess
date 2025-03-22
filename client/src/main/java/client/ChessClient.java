package client;

import server.ServerFacade;
import service.*;

import java.util.Arrays;

public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    private boolean isLoggedIn;
    private String user;
    private String authToken;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        isLoggedIn = false;
    }

    public String handleInput(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if(isLoggedIn) {
                return switch (cmd) {
                    case "logout" -> logout();
                    case "create" -> createGame(params);
                    case "list" -> list();
                    case "join" -> join(params);
                    case "observe" -> observe(params);
                    default -> help();
                };
            } else {
                return switch (cmd) {
                    case "login" -> login(params);
                    case "register" -> register(params);
                    case "quit" -> "quit";
                    default -> help();
                };
            }

        } catch (Exception ex) {
            return ex.getMessage();
        }
    }

    public String login(String... params) {
        if(params.length == 2) {
            var username = params[0];
            var pwd = params[1];
            try {
                var response = server.login(new LoginRequest(username, pwd));
                isLoggedIn = true;
                user = response.username();
                authToken = response.authToken();
                return String.format("You are logged in as %s.", response.username());
            } catch (Exception e) {
                return "TODO login throw 2 " + e.getMessage();
            }
        }
        return "TODO login throw";
    }
    public String register(String... params) throws Exception {
        if(params.length == 3) {
            var username = params[0];
            var pwd = params[1];
            var email = params[2];
            try {
                var response = server.register(new RegisterRequest(username, pwd, email));
                isLoggedIn = true;
                user = response.username();
                authToken = response.authToken();
                return String.format("Successfully registered and logged in as %s.", response.username());
            } catch (Exception e) {
                return "TODO register throw 2 " + e.getMessage();
            }
        }
        return "TODO register throw";
    }
    public String logout() {
        try {
            System.out.println(authToken);
            var response = server.logout(new LogoutRequest(authToken));
            isLoggedIn = false;
            return "Goodbye.";
        } catch (Exception e) {
            return "TODO logout throw 2 " + e.getMessage();
        }
    }
    public String createGame(String... params) {
        if(params.length == 1) {
            try {
                var name = params[0];
                var response = server.createGame(new CreateGameRequest(authToken, name));
                return String.format("Successfully created game %s with id %s.", name, response.gameID());
            } catch (Exception e) {
                return "TODO create Game throw 2 " + e.getMessage();
            }
        }
        return "TODO create Game throw";
    }
    public String list() {
        try {
            var response = server.listGames(new ListGameRequest(authToken));
            return "TODO format list response";//String.format("Successfully created game %s with id %s.", name, response.gameID());
        } catch (Exception e) {
            return "TODO list throw " + e.getMessage();
        }
    }
    public String join(String... params) {
        return "TODO join";
    }
    public String observe(String... params) {
        return "TODO observe";
    }

    public String help() {
        return ((!isLoggedIn)
                ? """
                    - register <username> <password> <email>
                    - login <username> <password>
                    - quit
                    - help
                    """
                : """
                    - create <name>
                    - list
                    - join <id> [WHITE|BLACK]
                    - observe <id>
                    - logout
                    - help
                    """
        );
    }
}
