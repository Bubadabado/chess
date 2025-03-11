package model;

import chess.ChessGame;

public record GameData(int gameID, String whiteUsername, String blackUsername,
                       String gameName, ChessGame game) {
    public GameData addColor(String username, String playerColor) {
        return ((playerColor.equals("white")) ? addWhite(username) : addBlack(username));
    }
    public String getColor(String playerColor) {
        return ((playerColor.equals("white")) ? whiteUsername : blackUsername );
    }
    GameData addWhite(String username) {
        return new GameData(gameID, username, blackUsername, gameName, game);
    }
    GameData addBlack(String username) {
        return new GameData(gameID, whiteUsername, username, gameName, game);
    }
}
