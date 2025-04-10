package server;

import chess.ChessGame;
import chess.ChessMove;
import websocket.messages.Notification;
import client.NotificationHandler;
import com.google.gson.Gson;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

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
                    ServerMessage smsg = new Gson().fromJson(message, ServerMessage.class);
//                    System.out.println(smsg.getServerMessageType());
//                    System.out.println(smsg.getNotification().message());
                    if(smsg.getServerMessageType() == ServerMessage.ServerMessageType.LOAD_GAME
                            && smsg.getGame() != null) {
                        messenger.reload(smsg.getGame());
                    }
                    Notification notification = smsg.getNotification();//new Gson().fromJson(smsg.getNotification().toString(), Notification.class);
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

    public void joinGame(String user, String color, String auth, int id, ChessGame game) throws Exception {
        try {
            var command = new UserGameCommand(user, color, auth, id, game);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
    public void observeGame(String user, String auth, int id) throws Exception {
        try {
            var command = new UserGameCommand(user, UserGameCommand.CommandType.OBSERVE, auth, id);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
    public void makeMove(String user, String color, ChessGame game, String move, String auth, int id) throws Exception {
        try {
            var command = new UserGameCommand(user, color, game, move, auth, id);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
    public void leave(String user, String auth, int id, String color) throws Exception {
        try {
            var command = new UserGameCommand(user, UserGameCommand.CommandType.LEAVE, auth, id, color);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
    public void resign(String user, String auth, int id, ChessGame game) throws Exception {
        try {
            var command = new UserGameCommand(user, game, UserGameCommand.CommandType.RESIGN, auth, id);
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new Exception(e.getMessage());
        }
    }
}
