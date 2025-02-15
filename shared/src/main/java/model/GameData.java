package model;

import chess.ChessGame;

public record GameData(int GameID, String whiteUsername, String blackUsername,
                       String gameName, ChessGame game) {
    public GameData addColor(String username, String playerColor) {
        return ((playerColor.equals("white")) ? addWhite(username) : addBlack(username));
    }
    public String getColor(String playerColor) {
        return ((playerColor.equals("white")) ? whiteUsername : blackUsername );
    }
    GameData addWhite(String username) {
        return new GameData(GameID, username, blackUsername, blackUsername, game);
    }
    GameData addBlack(String username) {
        return new GameData(GameID, whiteUsername, username, blackUsername, game);
    }
}
