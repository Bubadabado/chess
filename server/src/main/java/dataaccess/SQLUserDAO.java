package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var query = "SELECT username, password, email FROM users" +
                    "WHERE username = ?";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, username);
                var rs = preparedStatement.executeQuery();
                rs.next();
                return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to connect to DB on getUser");
        }
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var query = "INSERT INTO users (username, password, email) " +
                    "VALUES (?, ?, ?)";
            try (var preparedStatement = conn.prepareStatement(query)) {
                preparedStatement.setString(1, userData.username());
                preparedStatement.setString(2, userData.password());
                preparedStatement.setString(3, userData.email());
                var rs = preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DataAccessException("Error: failed to connect to DB on createUser");
        }
    }

    @Override
    public void clearUsers() {
        try (var conn = DatabaseManager.getConnection()) {
            var query = "DELETE FROM users";
            try (var preparedStatement = conn.prepareStatement(query)) {
                var rs = preparedStatement.executeUpdate();
            }
        } catch (SQLException | DataAccessException e) {}
    }
}
