package client;

import chess.ChessGame;
import chess.ChessPiece;
import chess.ChessPosition;
import model.GameData;
import server.ServerFacade;
import service.*;
import ui.EscapeSequences;

import java.util.Arrays;

public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    private boolean isLoggedIn;
    private String user;
    private String authToken;
    private ChessGame game;
    private String teamColor;

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
            return "Invalid input. Please try again.";//ex.getMessage();
        }
    }

    public String login(String... params) {
        int numParams = 2;
        if(params.length == numParams) {
            var username = params[0];
            var pwd = params[1];
            try {
                var response = server.login(new LoginRequest(username, pwd));
                isLoggedIn = true;
                user = response.username();
                authToken = response.authToken();
                return String.format("You are logged in as %s.", response.username());
            } catch (Exception e) {
                return "Login failed. Please check your password and try again.";// + e.getMessage();
            }
        }
        return "Login failed. Too " + ((params.length < numParams) ? "few " : "many ") + "parameters given.";
    }
    public String register(String... params) throws Exception {
        int numParams = 3;
        if(params.length == numParams) {
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
                return "Register failed. Username taken.";// + e.getMessage();
            }
        }
        return "Register failed. Too " + ((params.length < numParams) ? "few " : "many ") + "parameters given.";
    }
    public String logout() {
        try {
            var response = server.logout(new LogoutRequest(authToken));
            isLoggedIn = false;
            return "Goodbye. \n" + help();
        } catch (Exception e) {
            return "TODO logout throw 2 " + e.getMessage();
        }
    }
    public String createGame(String... params) {
        int numParams = 1;
        if(params.length == numParams) {
            try {
                var name = params[0];
                var response = server.createGame(new CreateGameRequest(authToken, name));
                return String.format("Successfully created game %s", name);
            } catch (Exception e) {
                return "Create failed.";// + e.getMessage();
            }
        }
        return "Create failed. Too " + ((params.length < numParams) ? "few " : "many ") + "parameters given.";
    }
    public String list() {
        try {
            var response = server.listGames(new ListGameRequest(authToken));
            StringBuilder result = new StringBuilder();
            int i = 0;
            for(GameData game : response.games()) {
                result.append(i);
                result.append(": ");
                result.append(game.gameName());
                result.append("\n");
                i++;
            }
            return result.toString();
        } catch (Exception e) {
            return "Failed to list games.";// + e.getMessage();
        }
    }
    public String join(String... params) {
        int numParams = 2;
        if(params.length == numParams) {
            try {
                var targetGame = server.listGames(
                        new ListGameRequest(authToken)).games().get(Integer.parseInt(params[0]));
                var id = targetGame.gameID();
                var color = params[1];
                var response = server.joinGame(new JoinGameRequest(authToken, color, id));
                game = targetGame.game();
                teamColor = color;
                return "Successfully joined game \n" + printGame();
            } catch (Exception e) {
                return "Failed to join game. Nonexistent game or color taken. Use \"list\" to view existing games";
            }
        }
        return "Join failed. Too " + ((params.length < numParams) ? "few " : "many ") + "parameters given.";
    }
    public String observe(String... params) {
        int numParams = 1;
        if(params.length == numParams) {
            try {
                var targetGame = server.listGames(
                        new ListGameRequest(authToken)).games().get(Integer.parseInt(params[0]));
                var id = targetGame.gameID();
                var response = server.observeGame(
                        new JoinGameRequest(authToken, null, Integer.parseInt(params[0])));
                game = targetGame.game();
                teamColor = "white";
                return "Observing game. \n" + printGame();
            } catch (Exception e) {
                return "Failed to observe game. Invalid id. ";// + e.getMessage();
            }
        }
        return "Observe failed. Too " + ((params.length < numParams) ? "few " : "many ") + "parameters given.";
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

    public String printGame() {
        return game.getBoard().toString((teamColor.equalsIgnoreCase("white"))
                ? ChessGame.TeamColor.WHITE
                : ChessGame.TeamColor.BLACK
        );
    }
    public boolean getLoginState() { return isLoggedIn; }
}
