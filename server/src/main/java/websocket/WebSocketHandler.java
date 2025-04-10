package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
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
            case CONNECT -> connect(command.getAuthToken(), session, command.getColor(), command.getUser());
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave(command.getAuthToken(), session, command.getUser());
            case RESIGN -> resign(command.getAuthToken(), session, command.getUser());
            case OBSERVE -> observe(command.getAuthToken(), session, command.getUser());
        }
    }

    private void connect(String auth, Session session, String color, String user) throws IOException {
        connections.add(auth, session);
        var message = String.format("%s joined the game as %s.", user, color);
        var notification = new ServerMessage(new Notification(message));//new Notification(Notification.Type.ARRIVAL, message);
        connections.broadcast(auth, notification);
    }
    private void observe(String auth, Session session, String user) throws IOException {
        connections.add(auth, session);
        var message = String.format("%s joined the game as an observer.", user);
        var notification = new ServerMessage(new Notification(message));//new Notification(Notification.Type.ARRIVAL, message);
        connections.broadcast(auth, notification);
    }
    private void makeMove() throws IOException {
        System.out.println("TODO makeMove");
    }
    private void leave(String auth, Session session, String user) throws IOException {
//        connections.add(auth, session);
        connections.remove(auth);
        var message = String.format("%s left the game.", user);
        var notification = new ServerMessage(new Notification(message));
        connections.broadcast(auth, notification);
    }
    private void resign(String auth, Session session, String user) throws IOException {
//        connections.add(auth, session);
        var message = String.format("%s resigned the game.", user);
        var notification = new ServerMessage(new Notification(message));
        connections.broadcast(auth, notification);
        connections.removeAll();
    }
}
