package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;

import java.util.ArrayList;
import java.util.Objects;

public class GameService {
    public static ListGameResult listGames(ListGameRequest listGameRequest) throws DataAccessException {
        var games = new MemoryGameDAO();
        try {
            if (listGameRequest.authToken() != null && checkAuth(listGameRequest.authToken())) {
                return new ListGameResult(games.listGames());
            } else {
                throw new DataAccessException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public static CreateGameResult createGame(CreateGameRequest createGameRequest) throws DataAccessException {
        var games = new MemoryGameDAO();
        try {
            if(createGameRequest.authToken() != null && checkAuth(createGameRequest.authToken())) {
                return new CreateGameResult(games.createGame(createGameRequest.gameName()));
            } else {
                throw new DataAccessException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public static void joinGame(JoinGameRequest joinGameRequest) throws DataAccessException {
        var auths = new MemoryAuthDAO();
        var games = new MemoryGameDAO();
        try {
            if(joinGameRequest.authToken() != null && checkAuth(joinGameRequest.authToken())) {
                if(joinGameRequest.playerColor() != null && colorNotTaken(joinGameRequest.gameID(), joinGameRequest.playerColor())) {
                    var username = auths.getAuth(joinGameRequest.authToken()).username();
                    games.joinGame(username, joinGameRequest.playerColor().toLowerCase(), joinGameRequest.gameID());
                } else {
                    throw new DataAccessException("Error: already taken");
                }
            } else {
                throw new DataAccessException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw e;
        }
    }

    private static boolean checkAuth(String authToken) throws DataAccessException {
        var auths = new MemoryAuthDAO();
        return auths.getAuth(authToken) != null;
    }
    private static boolean checkGame(int gameID) {
        var games = new MemoryGameDAO();
        return games.findGame(gameID) != null;
    }
    private static boolean colorNotTaken(int gameID, String playerColor) {
        if(playerColor == null) { return false; }
        var games = new MemoryGameDAO();
        playerColor = playerColor.toLowerCase();
        return checkGame(gameID)
                && (playerColor.equals("black") || playerColor.equals("white"))
                && games.getColor(playerColor, gameID) == null;
    }
}
