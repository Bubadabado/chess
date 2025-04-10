package websocket.commands;

import chess.ChessGame;
import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    private final CommandType commandType;

    private final String authToken;

    private final Integer gameID;

    private final String color;

    private final String userName;

    private final ChessGame game;

    private final String move;

    public UserGameCommand(CommandType commandType, String authToken, Integer gameID) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.color = "";
        this.userName = "";
        this.game = new ChessGame();
        this.move = "";
    }
    //connect
    public UserGameCommand(String userName, String color, String authToken, Integer gameID) {
        this.commandType = CommandType.CONNECT;
        this.authToken = authToken;
        this.gameID = gameID;
        this.color = color;
        this.userName = userName;
        this.game = new ChessGame();
        this.move = "";
    }
    //make move
    public UserGameCommand(String userName, String color, ChessGame game, String move, String authToken, Integer gameID) {
        this.commandType = CommandType.MAKE_MOVE;
        this.authToken = authToken;
        this.gameID = gameID;
        this.color = color;
        this.userName = userName;
        this.game = game;
        this.move = move;
    }
    //other
    public UserGameCommand(String userName, CommandType commandType, String authToken, Integer gameID) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.color = "";
        this.userName = userName;
        this.game = new ChessGame();
        this.move = "";
    }
    public UserGameCommand(String userName, ChessGame game, CommandType commandType, String authToken, Integer gameID) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.color = "";
        this.userName = userName;
        this.game = game;
        this.move = "";
    }
    public UserGameCommand(String userName, CommandType commandType, String authToken, Integer gameID, String color) {
        this.commandType = commandType;
        this.authToken = authToken;
        this.gameID = gameID;
        this.color = color;
        this.userName = userName;
        this.game = new ChessGame();
        this.move = "";
    }

    public enum CommandType {
        CONNECT,
        MAKE_MOVE,
        LEAVE,
        RESIGN,
        OBSERVE
    }

    public CommandType getCommandType() {
        return commandType;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Integer getGameID() {
        return gameID;
    }

    public String getColor() { return color; }

    public String getUser() { return userName; }

    public ChessGame getGame() { return game; }

    public String getMove() { return move; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserGameCommand)) {
            return false;
        }
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() &&
                Objects.equals(getAuthToken(), that.getAuthToken()) &&
                Objects.equals(getGameID(), that.getGameID());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthToken(), getGameID());
    }
}
