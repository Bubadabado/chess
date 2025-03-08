package dataaccess;

import model.UserData;

import java.sql.SQLException;

public class SQLUserDAO implements UserDAO{
    //TODO: bCrypt
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

    }

    @Override
    public void clearUsers() {

    }
}
