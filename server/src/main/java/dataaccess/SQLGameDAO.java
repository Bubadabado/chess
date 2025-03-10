package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{
    @Override
    public int createGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var query = "INSERT INTO games (name) " +
                    "VALUES(?)";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, gameName);
                //TODO: include game json
                var json = new Gson().toJson(new ChessGame());
                var rs = preparedStatement.executeUpdate();
                query = "SELECT id FROM games WHERE name = ?";
                try (var ps = conn.prepareStatement(query)) {
                    preparedStatement.setString(1, gameName);
                    var nrs = preparedStatement.executeQuery();
                    nrs.next();
                    return nrs.getInt("name");
                }

            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to connect to DB on createGame");
        }
    }

    @Override
    public GameData findGame(int gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            var query = "SELECT * FROM games " +
                    "WHERE id = ?";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, gameID);
                var rs = preparedStatement.executeQuery();
                boolean empty = !rs.next();
                if(empty) {
                    return null;
                }
                return new GameData(rs.getInt("id"), rs.getString("white_username"), rs.getString("black_username"), rs.getString("name"));
            }
        } catch (SQLException e) {
//            throw new DataAccessException("Error: failed to connect to DB on findGame");
        }  catch (DataAccessException e) {
//            throw new RuntimeException(e);
        }
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
                var rs = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
//            throw new DataAccessException("Error: failed to connect to DB on joinGame");
        } catch (DataAccessException e) {
//            throw new RuntimeException(e);
        }
    }

    @Override
    public String getColor(String playerColor, int gameID) {
        try (var conn = DatabaseManager.getConnection()) {
            if(!playerColor.equals("black") && !playerColor.equals("white")) {
                throw new DataAccessException("invalid colors");
            }
            String col = playerColor + "_username";
            var query = "SELECT " + col + " FROM games " +
                    "WHERE id = ?";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setInt(1, gameID);
                var rs = preparedStatement.executeQuery();
                rs.next();
                return rs.getString(col);
            }
        } catch (SQLException e) {
//            throw new DataAccessException("Error: failed to connect to DB on getColor");
        } catch (DataAccessException e) {
//            throw new RuntimeException(e);
        }
        return null;
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
