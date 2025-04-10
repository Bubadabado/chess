package client;

import chess.ChessGame;
import websocket.messages.Notification;

public interface NotificationHandler {
    void notify(String n);
    void reload(ChessGame g);
}
