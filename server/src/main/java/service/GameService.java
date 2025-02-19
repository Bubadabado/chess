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
        if(checkAuth(createGameRequest.authToken())) {
            return new CreateGameResult(games.createGame(createGameRequest.gameName()));
        } else {
            return new CreateGameResult(-1);
        }
    }
    public static void joinGame(JoinGameRequest joinGameRequest) {

    }

    private static boolean checkAuth(String authToken) {
        var auths = new MemoryAuthDAO();
        return auths.getAuth(authToken) != null;
    }
}
