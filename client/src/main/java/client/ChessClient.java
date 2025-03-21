package client;

import server.ServerFacade;
import service.LoginRequest;
import service.RegisterRequest;

import java.util.Arrays;

public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    private boolean isLoggedIn;
    private String user;

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
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "logout" -> logout();
                case "create" -> createGame(params);
                case "list" -> list();
                case "join" -> join(params);
                case "observe" -> observe(params);
                case "quit" -> "quit";
                default -> help();
            };
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
                return String.format("Successfully registered and logged in as %s.", response.username());
            } catch (Exception e) {
                return "TODO register throw 2 " + e.getMessage();
            }
        }
        return "TODO register throw";
    }
    public String logout() {
        return "TODO logout";
    }
    public String createGame(String... params) {
        return "TODO createGame";
    }
    public String list() {
        return "TODO list";
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
