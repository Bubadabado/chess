package client;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPosition;
import model.GameData;
import server.ServerFacade;
import service.*;

import java.util.Arrays;

public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    private boolean isLoggedIn;
    private String user;
    private String authToken;
    private ChessGame game;
    private String teamColor;
    private boolean isInGame;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        isLoggedIn = false;
        isInGame = false;
    }

    public String handleInput(String input) {
        try {
            var tokens = input.toLowerCase().split(" ");
            var cmd = (tokens.length > 0) ? tokens[0] : "help";
            var params = Arrays.copyOfRange(tokens, 1, tokens.length);
            if(isInGame) {
                return switch (cmd) {
                    case "redraw" -> printGame();
                    case "leave" -> leaveGame();
                    case "move" -> makeMove(params);
                    case "resign" -> resign();
                    case "highlight" -> highlight(params);
                    default -> help();
                };
            } else if(isLoggedIn) {
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
            //TODO: leave game
            isInGame = false;
            return "Goodbye. \n" + help();
        } catch (Exception e) {
            return "Logout failed.";// + e.getMessage();
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
            if (response.games().isEmpty()) {
                return  "There are no current games.";
            }
            StringBuilder result = new StringBuilder();
            int i = 0;
            for(GameData game : response.games()) {
                result.append(i);
                result.append(": ");
                result.append(game.gameName());
                result.append(" - White: ");
                result.append((game.whiteUsername() == null) ? "(empty)" : game.whiteUsername());
                result.append(", Black: ");
                result.append((game.blackUsername() == null) ? "(empty)" : game.blackUsername());
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
                isInGame = true;
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
        return ((isInGame)
                ?  """
                    - redraw
                    - leave
                    - move <from> <to>
                    - resign
                    - highlight <from>
                    - help
                    
                    *coords format is a letter followed by a number, ex. c2
                    """
                : ((isLoggedIn)
                    ?  """
                        - create <name>
                        - list
                        - join <id> [WHITE|BLACK]
                        - observe <id>
                        - logout
                        - help
                        """
                    : """
                        - register <username> <password> <email>
                        - login <username> <password>
                        - quit
                        - help
                        """
        ));
    }

    public String printGame() {
        return game.getBoard().toString(stringToColor(teamColor));
    }
    private ChessGame.TeamColor stringToColor(String tc) {
        return (tc.equalsIgnoreCase("white"))
                ? ChessGame.TeamColor.WHITE
                : ChessGame.TeamColor.BLACK;
    }
    public String redraw() {
        try {
            return printGame();
        } catch (Exception e) {
            return "Redraw failed.";// + e.getMessage();
        }
    }
    public String leaveGame() {
        try {
            isInGame = false;
            //TODO: quit game
            return "todo leave game";
        } catch (Exception e) {
            return "leave failed.";// + e.getMessage();
        }
    }
    public String makeMove(String... params) {
        int numParams = 2;
        if(params.length == numParams) {
            try {
//                var name = params[0];
//                var response = server.createGame(new CreateGameRequest(authToken, name));
//                return String.format("Successfully created game %s", name);
                return "TODO make move";
            } catch (Exception e) {
                return "Make move failed.";// + e.getMessage();
            }
        }
        return "Make move failed. Too " + ((params.length < numParams) ? "few " : "many ") + "parameters given.";
    }
    public String resign() {
        try {
            return "todo resign";
        } catch (Exception e) {
            return "Resign failed.";// + e.getMessage();
        }
    }
    public String highlight(String... params) {
        int numParams = 1;
        if(params.length == numParams) {
            try {
//                var name = params[0];
//                var response = server.createGame(new CreateGameRequest(authToken, name));
//                return String.format("Successfully created game %s", name);
                var coords = getCoords(params[0]); //col, row
                var col = stringToColor(teamColor);
                var moves = game.validMoves(new ChessPosition(coords[1], coords[0]));
                return game.getBoard().toString(col, moves);
            } catch (Exception e) {
                return "Highlight failed. Invalid parameters given.";// + e.getMessage();
            }
        }
        return "Highlight failed. Too " + ((params.length < numParams) ? "few " : "many ") + "parameters given.";
    }

    private int[] splitCoords(String coords) {
        return new int[]{(coords.charAt(0) - 'a') + 1, coords.charAt(1) - '0'};
    }

    /**
     * parses a coord string and returns in the format of int[col, row]
     * @param coords
     * @return
     */
    private int[] getCoords(String coords) {
        var ncoords = splitCoords(coords);
        ncoords[0] = (stringToColor(teamColor) == ChessGame.TeamColor.WHITE)
                ? ncoords[0]
                : ChessBoard.BOARD_SIZE - ncoords[0] + 1;
        return ncoords;
    }

    public boolean getInGameState() { return isInGame; }
    public boolean getLoginState() { return isLoggedIn; }
}
