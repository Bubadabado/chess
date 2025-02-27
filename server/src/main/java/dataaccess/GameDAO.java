package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GameDAO {
    public int createGame(String gameName) throws DataAccessException;
    public GameData findGame(int gameID);
    public void joinGame(String username, String playerColor, int gameID);
    public String getColor(String playerColor, int gameID);
    public ArrayList<GameData> listGames();
    public void clearGames();
}
