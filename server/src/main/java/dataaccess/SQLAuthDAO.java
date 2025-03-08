package dataaccess;

import model.AuthData;
import model.UserData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO{
    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var query = "INSERT INTO auths (authtoken, username)" +
                    "VALUES(?, ?)";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, authData.authToken());
                preparedStatement.setString(2, authData.username());
                var rs = preparedStatement.executeUpdate();
                rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to connect to DB on createAuth");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var query = "SELECT authtoken, username FROM auths" +
                    "WHERE authtoken = ?";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeQuery();
                rs.next();
                return new AuthData(rs.getString("authtoken"), rs.getString("username"));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to connect to DB on getAuth");
        }
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var query = "DELETE FROM auths" +
                    "WHERE authtoken = ?";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, authToken);
                var rs = preparedStatement.executeUpdate();
                rs.next();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to connect to DB on deleteAuth");
        }
    }

    @Override
    public void clearAuths() {
        try (var conn = DatabaseManager.getConnection()) {
            var query = "DELETE FROM auths";
            try (var preparedStatement = conn.prepareStatement(query)) {
                var rs = preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {}
    }
}
