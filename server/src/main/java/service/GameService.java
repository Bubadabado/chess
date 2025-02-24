package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;

import java.util.ArrayList;

public class GameService {
    public static ListGameResult listGames(ListGameRequest listGameRequest) {
        var games = new MemoryGameDAO();
        if(checkAuth(listGameRequest.authToken())) {
            return new ListGameResult(games.listGames());
        } else {
            return new ListGameResult(new ArrayList<>());
        }
    }
    public static CreateGameResult createGame(CreateGameRequest createGameRequest) {
        var games = new MemoryGameDAO();
        System.out.println(createGameRequest.authToken());
        System.out.println(checkAuth(createGameRequest.authToken()));
        if(checkAuth(createGameRequest.authToken())) {
            return new CreateGameResult(games.createGame(createGameRequest.gameName()));
        } else {
            return new CreateGameResult(-1);
        }
    }
    public static void joinGame(JoinGameRequest joinGameRequest) {
        var auths = new MemoryAuthDAO();
        var games = new MemoryGameDAO();
        if(checkAuth(joinGameRequest.authToken())
                && colorNotTaken(joinGameRequest.gameID(), joinGameRequest.playerColor())) {
            var username = auths.getAuth(joinGameRequest.authToken()).username();
            games.joinGame(username, joinGameRequest.playerColor(), joinGameRequest.gameID());
        }
    }

    private static boolean checkAuth(String authToken) {
        var auths = new MemoryAuthDAO();
        System.out.println(auths);
        return auths.getAuth(authToken) != null;
    }
    private static boolean checkGame(int gameID) {
        var games = new MemoryGameDAO();
        return games.findGame(gameID) != null;
    }
    private static boolean colorNotTaken(int gameID, String playerColor) {
        var games = new MemoryGameDAO();
        return checkGame(gameID) && games.getColor(playerColor, gameID).isEmpty();
    }
}
