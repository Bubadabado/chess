package dataaccess;

import model.AuthData;
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
        try (var conn = DatabaseManager.getConnection()) {
            //sanity check input
            if(!playerColor.equals("black") && !playerColor.equals("white")) {
                throw new DataAccessException("invalid colors");
            }
            var query = "UPDATE games " +
                    "SET " + playerColor + "_username = ? " +
                    "WHERE id = ?";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                preparedStatement.setInt(2, gameID);
                var rs = preparedStatement.executeQuery();
            }
        } catch (SQLException e) {
//            throw new DataAccessException("Error: failed to connect to DB on getAuth");
        } catch (DataAccessException e) {
//            throw new RuntimeException(e);
        }
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
