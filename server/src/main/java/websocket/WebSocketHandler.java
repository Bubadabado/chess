package websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.UserGameCommand;
import websocket.messages.Notification;

import java.io.IOException;

@WebSocket
public class WebSocketHandler {

    private final ConnectionManager connections = new ConnectionManager();

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case CONNECT -> connect(command.getAuthToken(), session);
            case MAKE_MOVE -> makeMove();
            case LEAVE -> leave();
            case RESIGN -> resign();
        }
    }

    private void connect(String auth, Session session) throws IOException {
        connections.add(auth, session);
        var message = String.format("%s joined the game.", "TODO username");
        var notification = new Notification(message);//new Notification(Notification.Type.ARRIVAL, message);
        connections.broadcast(auth, notification);
    }
    private void makeMove() throws IOException {
        System.out.println("TODO makeMove");
    }
    private void leave() throws IOException {
        System.out.println("TODO leave");
    }
    private void resign() throws IOException {
        System.out.println("TODO resign");
    }
}
