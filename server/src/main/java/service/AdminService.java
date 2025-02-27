package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class AdminService {
    public static boolean clear() {
        clearAuths();
        clearUsers();
        clearGames();
        return true;
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
}
