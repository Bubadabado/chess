package server;

import websocket.messages.Notification;
import client.NotificationHandler;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//@ClientEndpoint
public class WebSocketFacade extends Endpoint {
    Session session;
    NotificationHandler messenger;


    public WebSocketFacade(String url, NotificationHandler messenger) throws Exception {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.messenger = messenger;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    Notification notification = new Gson().fromJson(message, Notification.class);
                    messenger.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new Exception(e.getMessage());//(500, ex.getMessage());
        }
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    public void joinGame(String auth, int id) throws Exception {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, auth, id);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
    public void makeMove(String auth, int id) throws Exception {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.MAKE_MOVE, auth, id);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
    public void leave(String auth, int id) throws Exception {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, auth, id);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
    public void resign(String auth, int id) throws Exception {
        try {
            var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, auth, id);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
}
