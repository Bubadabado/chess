package websocket;

import chess.ChessGame;
import chess.ChessMove;
import chess.EscapeSequencesShared;
import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import records.UpdateGameRequest;
import service.GameService;
import websocket.commands.UserGameCommand;
import websocket.messages.Notification;
import websocket.messages.ServerMessage;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), session,
                    command.getColor(), command.getUser(), command.getGameID());
            case MAKE_MOVE -> makeMove(command.getAuthToken(), session, command.getGameID(),
                    command.getColor(), command.getUser(), command.getGame(), command.getMove());
            case LEAVE -> leave(command.getAuthToken(), session,
                    command.getUser(), command.getGameID(), command.getColor());
            case RESIGN -> resign(command.getAuthToken(), session,
                    command.getUser(), command.getGameID(), command.getGame());
            case OBSERVE -> observe(command.getAuthToken(), session,
                    command.getUser(), command.getGameID());
        }
    }

    private void connect(String auth, Session session, String color, String user, int id) throws IOException {
        connections.add(auth, session, id);
        var message = String.format("%s joined the game as %s.", user, color);
        var notification = new ServerMessage(new Notification(message));
        connections.broadcast(auth, notification, id);
    }
    private void observe(String auth, Session session, String user, int id) throws IOException {
        connections.add(auth, session, id);
        var message = String.format("%s joined the game as an observer.", user);
        var notification = new ServerMessage(new Notification(message));
        connections.broadcast(auth, notification, id);
    }
    private void makeMove(String auth, Session session, int id,
                          String color, String user, ChessGame game, String move) throws IOException {
        StringBuilder msg = new StringBuilder();
        if(game.isGameOver()) {
            msg.append("Error: game is over.");
            connections.broadcastOne(auth,
                    new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                            new Notification(msg.toString())));
        }
        else if((game.getTeamTurn() == ChessGame.TeamColor.WHITE) == (color.equalsIgnoreCase("white"))) {
            //checks opposite as the move will have already been made locally
            msg.append("Error: not your turn.");
            connections.broadcastOne(auth,
                    new ServerMessage(ServerMessage.ServerMessageType.ERROR,
                            new Notification(msg.toString())));
        } else {
            GameService.updateGame(new UpdateGameRequest(auth, id, game));
            msg.append("\n");
//            msg.append(game.getBoard().toString((color.equalsIgnoreCase("white"))
//                    ? ChessGame.TeamColor.WHITE
//                    : ChessGame.TeamColor.BLACK));
//            msg.append("\n");
            msg.append(EscapeSequencesShared.SET_TEXT_COLOR_BLUE);
            msg.append(String.format("%s made move: %s.", user, move));
            if(game.isInStalemate(game.getTeamTurn())) {
                msg.append(String.format("\n%s is in stalemate! Game over.",
                        (game.getTeamTurn() == ChessGame.TeamColor.WHITE)
                                ? "White" : "Black"));
            } else if(game.isInCheckmate(game.getTeamTurn())) {
                msg.append(String.format("\n%s is in checkmate! Game over.",
                        (game.getTeamTurn() == ChessGame.TeamColor.WHITE)
                                ? "White" : "Black"));
            } else if(game.isInCheck(game.getTeamTurn())) {
                msg.append(String.format("\n%s is in check!", (game.getTeamTurn() == ChessGame.TeamColor.WHITE)
                        ? "White" : "Black"));
            }
            connections.broadcast("", new ServerMessage(game,
                    new Notification(msg.toString())), id);
        }
    }
    private void leave(String auth, Session session, String user, int id, String col) throws IOException {
        connections.remove(auth);
        GameService.leaveGame(auth, id, user, col);
        var message = String.format("%s left the game.", user);
        var notification = new ServerMessage(new Notification(message));
        connections.broadcast(auth, notification, id);
    }
    private void resign(String auth, Session session, String user, int id, ChessGame game) throws IOException {
        var message = String.format("%s resigned the game.", user);
        game.setGameOver(true);
        GameService.updateGame(new UpdateGameRequest(auth, id, game));
        var notification = new ServerMessage(new Notification(message));
        connections.broadcast(auth, notification, id);
        connections.removeAll();
    }
}
