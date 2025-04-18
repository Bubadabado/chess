package server;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import websocket.messages.*;
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
                    LoadGameMessage smsg = new Gson().fromJson(message, LoadGameMessage.class);
                    if(smsg.getGame() == null) {
                        NotificationMessage nmsg = new Gson().fromJson(message, NotificationMessage.class);
                        if(nmsg.getMessage() == null) {
                            ErrorMessage emsg = new Gson().fromJson(message, ErrorMessage.class);
                            messenger.notify(emsg.getErrorMessage());
                            return;
                        }
                        messenger.notify(nmsg.getMessage());
                        return;
                    }
                    messenger.reload(smsg.getGame());
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
    public void makeMove(ChessMove move, String cm, String auth, int id) throws Exception {
        try {
            var command = new UserGameCommand(auth, id, move);
            command.setCm(cm);
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
