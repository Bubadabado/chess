package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    public int createGame(String gameName);
    public GameData findGame(String gameID);
    public void joinGame(String playerColor, int gameID);
    public void getColor(String playerColor, int gameID);
    public ArrayList<GameData> listGames();
    public void clearGames();
}
