package dataaccess;

import chess.ChessGame;
import model.GameData;
import model.UserData;

import java.util.ArrayList;
import java.util.Hashtable;

public class MemoryGameDAO implements GameDAO {
    private static final Hashtable<Integer, GameData> games = new Hashtable<>();
    private static int runningID = 0;

    @Override
    public int createGame(String gameName) {
        games.put(runningID, new GameData(runningID, "", "", gameName, new ChessGame()));
        runningID++;
        return runningID - 1;
    }

    @Override
    public GameData findGame(int gameID) {
        return games.get(gameID);
    }

    @Override
    public void joinGame(String username, String playerColor, int gameID) {
        games.put(gameID, games.get(gameID).addColor(username, playerColor));
    }

    @Override
    public String getColor(String playerColor, int gameID) {
        return games.get(gameID).getColor(playerColor);
    }

    @Override
    public ArrayList<GameData> listGames() {
        return new ArrayList<>(games.values());
    }

    @Override
    public void clearGames() {
        games.clear();
    }

    @Override
    public String toString() {
        return games.toString();
    }
}
