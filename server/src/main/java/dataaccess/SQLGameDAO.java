package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;

import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

public class SQLGameDAO implements GameDAO{
    @Override
    public int createGame(String gameName) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var json = new Gson().toJson(new ChessGame());
            var query = "INSERT INTO games (name, game) " +
                    "VALUES(?,?)";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, gameName);
                preparedStatement.setString(2, json);
                var rs = preparedStatement.executeUpdate();
                query = "SELECT id FROM games WHERE name = ?";
                try (var ps = conn.prepareStatement(query)) {
                    ps.setString(1, gameName);
                    var nrs = ps.executeQuery();
                    nrs.next();
                    return nrs.getInt("id");
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
                var json = rs.getString("game");
//                System.out.println(json);
                var game = new Gson().fromJson(json, ChessGame.class);
                return new GameData(rs.getInt("id"), rs.getString("white_username"), rs.getString("black_username"), rs.getString("name"), game);
            }
        } catch (SQLException e) {
//            throw new DataAccessException("Error: failed to connect to DB on findGame");
        }  catch (DataAccessException e) {
//            throw new RuntimeException(e);
        }
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
        ArrayList<GameData> games = new ArrayList<>();
        try (var conn = DatabaseManager.getConnection()) {
            var query = "SELECT * FROM games";
            try (var preparedStatement = conn.prepareStatement(query)) {
                var rs = preparedStatement.executeQuery();
                while(rs.next())
                {
                    var game = new Gson().fromJson(rs.getString("game"), ChessGame.class);
                    games.add(new GameData(rs.getInt("id"),
                            rs.getString("white_username"),
                            rs.getString("black_username"),
                            rs.getString("name"), game));
                }
            }
        } catch (SQLException e) {
//            throw new DataAccessException("Error: failed to connect to DB on findGame");
        }  catch (DataAccessException e) {
//            throw new RuntimeException(e);
        }
        return games;
    }

    public void updateGame(int id, ChessGame game) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var json = new Gson().toJson(game);
            var query = "UPDATE games " +
                    "SET game = ? " +
                    "WHERE id = ? ";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, json);
                preparedStatement.setInt(2, id);
                var rs = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to connect to DB on update");
        }
    }

    public void leaveGame(int id, String user, String playerColor) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            if(!playerColor.equals("black") && !playerColor.equals("white")) {
                throw new DataAccessException("invalid colors");
            }
            String col = playerColor + "_username";
            var query = "UPDATE games " +
                    "SET " + col + " = ? " +
                    "WHERE id = ? ";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setNull(1, Types.VARCHAR);
                preparedStatement.setInt(2, id);
                var rs = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("error: " + e.getMessage());
            throw new DataAccessException("Error: failed to connect to DB on leave game");
        }
    }

    @Override
    public void clearGames() {
        try (var conn = DatabaseManager.getConnection()) {
            var query = "DELETE FROM games";
            try (var preparedStatement = conn.prepareStatement(query)) {
                var rs = preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {}
    }
}
