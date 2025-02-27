package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class AdminService {
    public static void clear() {
        clearAuths();
        clearUsers();
        clearGames();
    }
    private static void clearAuths() {
        var auths = new MemoryAuthDAO();
        auths.clearAuths();
    }
    private static void clearUsers() {
        var users = new MemoryUserDAO();
        users.clearUsers();
    }
    private static void clearGames() {
        var games = new MemoryGameDAO();
        games.clearGames();
    }

    private static boolean checkAuth(String authToken) throws DataAccessException {
        var auths = new MemoryAuthDAO();
        return auths.getAuth(authToken) != null;
    }

}
