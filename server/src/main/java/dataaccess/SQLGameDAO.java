package dataaccess;

import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{
    @Override
    public int createGame(String gameName) throws DataAccessException {
        return 0;
    }

    @Override
    public GameData findGame(int gameID) {
        return null;
    }

    @Override
    public void joinGame(String username, String playerColor, int gameID) {

    }

    @Override
    public String getColor(String playerColor, int gameID) {
        return "";
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {
        return null;
    }

    @Override
    public void clearGames() {
        try (var conn = DatabaseManager.getConnection()) {
            var query = "DELETE FROM games";
            try (var preparedStatement = conn.prepareStatement(query)) {
                var rs = preparedStatement.executeQuery();
            }
        } catch (SQLException | DataAccessException e) {}
    }
}
