package websocket.messages;

import chess.ChessGame;

import java.util.Objects;

/**
 * Represents a Message the server can send through a WebSocket
 * 
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class ServerMessage {
    ServerMessageType serverMessageType;
//    private ChessGame game;
    private String message;

    public enum ServerMessageType {
        LOAD_GAME,
        ERROR,
        NOTIFICATION
    }

    public ServerMessage(ServerMessageType type) {
        this.serverMessageType = type;
    }
//    public ServerMessage(ServerMessageType type, ChessGame g) {
//        this.serverMessageType = type;
//        this.game = g;
//    }

//    public ServerMessage(ChessGame game, Notification notification) {
//        this.serverMessageType = ServerMessageType.LOAD_GAME;
////        this.game = game;
//        this.notification = notification;
//    }
    public ServerMessage(ServerMessageType type, String message) {
        this.serverMessageType = type;
        this.message = message;
//        this.game = null;
    }
//
    public ServerMessage(String message) {
        this.serverMessageType = ServerMessageType.NOTIFICATION;
        this.message = message;
//        this.game = null;
    }

//    public Notification getNotification() { return this.notification; }

    public ServerMessageType getServerMessageType() {
        return this.serverMessageType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ServerMessage)) {
            return false;
        }
        ServerMessage that = (ServerMessage) o;
        return getServerMessageType() == that.getServerMessageType();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getServerMessageType());
    }
}
