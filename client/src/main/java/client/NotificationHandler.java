package client;

import chess.ChessGame;
import websocket.messages.Notification;

public interface NotificationHandler {
    void notify(Notification n);
    void reload(ChessGame g);
}
