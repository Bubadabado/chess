package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.EscapeSequencesShared;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import records.UpdateGameRequest;
import service.GameService;
import service.ListGameRequest;
import service.UserService;
import websocket.commands.UserGameCommand;
import websocket.messages.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        System.out.println(message);
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        System.out.println(command.getCommandType());
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), session, command.getGameID());
            case MAKE_MOVE -> makeMove(command.getAuthToken(), session, command.getGameID(),
                    command.getColor(), command.getCm(), command.getMove());
            case LEAVE -> leave(command.getAuthToken(), session, command.getGameID());
            case RESIGN -> resign(command.getAuthToken(), session, command.getGameID());
        }
    }

    private void connect(String auth, Session session, int id) throws IOException {
        System.out.println("connecting");
        var user = getUser(auth);
        try {
            connections.add(auth, session, id);
            String color = userColorInGame(auth, id, user);
            var message = String.format("%s joined the game as %s.", user, (color.isEmpty()) ? "an observer" : color);
            var game = getGame(auth, id);
            System.out.println(game);
            var n = new LoadGameMessage(game);
            connections.broadcastOne(auth, n);
            var notification = new NotificationMessage(message);
            connections.broadcast(auth, notification, id);
        } catch (IOException | DataAccessException e) {
            var n = new ErrorMessage("Error: Unauthorized.");
            connections.broadcastSession(session, n);
        } catch (NoSuchElementException e) {
            var n = new ErrorMessage("Error: Game does not exist.");
            connections.broadcastOne(auth, n);
        }
    }
    private void resign(String auth, Session session, int id) throws IOException {
        var user = getUser(auth);
        try {
            if(userColorInGame(auth, id, user).isEmpty()) {
                var n = new ErrorMessage("Error: Observers cannot resign.");
                connections.broadcastOne(auth, n);
                return;
            }
            var game = getGame(auth, id);
            if(game.isGameOver()) {
                var n = new ErrorMessage("Error: The game is over.");
                connections.broadcastOne(auth, n);
                return;
            }
            game.setGameOver(true);
            GameService.updateGame(new UpdateGameRequest(auth, id, game));
            var notification = new NotificationMessage(String.format("%s resigned the game.", user));
            connections.broadcast("", notification, id);
        } catch (IOException | DataAccessException e) {
            var n = new ErrorMessage("Error: Unauthorized.");
            connections.broadcastSession(session, n);
        } catch (NoSuchElementException e) {
            var n = new ErrorMessage("Error: Game does not exist.");
            connections.broadcastOne(auth, n);
        }
    }
    private void leave(String auth, Session session, int id) throws IOException {
        var user = getUser(auth);
        try {
            System.out.println(user + " leaving");
            connections.remove(auth);
            String col = userColorInGame(auth, id, user);
            if(!col.isEmpty()) {
                GameService.leaveGame(auth, id, user, col);
            }
            var notification = new NotificationMessage(String.format("%s left the game.", user));
            connections.broadcast(auth, notification, id);
        } catch (IOException | DataAccessException e) {
            var n = new ErrorMessage("Error: Unauthorized.");
            connections.broadcastSession(session, n);
        } catch (NoSuchElementException e) {
            var n = new ErrorMessage("Error: Game does not exist.");
            connections.broadcastOne(auth, n);
        }
    }

    private String getUser(String auth) {
        try {
            return UserService.getUser(auth);
        } catch (DataAccessException e) {
            return "";
        }
    }
    private ChessGame getGame(String auth, int id) throws DataAccessException, NoSuchElementException {
        return GameService.listGames(new ListGameRequest(auth)).games().stream().filter((g) -> {
            return g.gameID() == id;
        }).toList().getFirst().game();
    }
    private String userColorInGame(String auth, int id, String user) {
        try {
            var game = GameService.listGames(new ListGameRequest(auth)).games().stream().filter((g) -> {
                return g.gameID() == id;
            }).toList().getFirst();
            return ((game.whiteUsername() != null) && game.whiteUsername().equals(user))
                    ? "white"
                    : ((game.blackUsername() != null) && (game.blackUsername().equals(user))
                        ? "black"
                        : ""
            );
        } catch (DataAccessException | NoSuchElementException e) {
            return "";
        }
    }
    private void makeMove(String auth, Session session, int id,
                          String color, String move, ChessMove cm) throws IOException {
        var user = getUser(auth);
        try {
            if(!connections.hasConnection(auth)) {
                var n = new ErrorMessage("Error: Unauthorized.");
                connections.broadcastOne(auth, n);
                return;
            }
            var game = getGame(auth, id);
            if(game.isGameOver()) {
                var n = new ErrorMessage("Error: The game is over.");
                connections.broadcastOne(auth, n);
                return;
            }
            if(userColorInGame(auth, id, user).isEmpty()) {
                var n = new ErrorMessage("Error: Observers cannot make moves.");
                connections.broadcastOne(auth, n);
                return;
            }
            if(!userColorInGame(auth, id, user).equals((game.getTeamTurn() == ChessGame.TeamColor.WHITE)
                    ? "white" : "black")) {
                var n = new ErrorMessage("Error: Out of turn.");
                connections.broadcastOne(auth, n);
                return;
            }
            game.makeMove(cm);
            GameService.updateGame(new UpdateGameRequest(auth, id, game));
            var n = new LoadGameMessage(game);
            connections.broadcast("", n, id);
            var notification = new NotificationMessage(String.format("%s made move: %s.", user, move));
            connections.broadcast(auth, notification, id);
            if(game.isInStalemate(game.getTeamTurn())) {
                var nm = new NotificationMessage(String.format("\n%s is in stalemate! Game over.",
                        (game.getTeamTurn() == ChessGame.TeamColor.WHITE)
                                ? "White" : "Black"));
                connections.broadcast("", nm, id);
            } else if (game.isInCheckmate(game.getTeamTurn())) {
                var nm = new NotificationMessage(String.format("\n%s is in checkmate! Game over.",
                        (game.getTeamTurn() == ChessGame.TeamColor.WHITE)
                                ? "White" : "Black"));
                connections.broadcast("", nm, id);
            } else if (game.isInCheck(game.getTeamTurn())) {
                var nm = new NotificationMessage(String.format("\n%s is in check!",
                        (game.getTeamTurn() == ChessGame.TeamColor.WHITE)
                                ? "White" : "Black"));
                connections.broadcast("", nm, id);
            }
        } catch (IOException | DataAccessException | NullPointerException e) {
            var n = new ErrorMessage("Error: Unauthorized.");
            connections.broadcastSession(session, n);
        } catch (NoSuchElementException e) {
            var n = new ErrorMessage("Error: Game does not exist.");
            connections.broadcastOne(auth, n);
        } catch (InvalidMoveException e) {
            var n = new ErrorMessage("Error: Invalid move.");
            connections.broadcastOne(auth, n);
        }
    }
}
