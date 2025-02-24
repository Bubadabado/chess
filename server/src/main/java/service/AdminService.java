package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class AdminService {
    public static void clear() {
        clearAuths();
        clearUsers();
        clearGames();
    }
    public static void clearAuths() {
        var auths = new MemoryAuthDAO();
        auths.clearAuths();
    }
    public static void clearUsers() {
        var users = new MemoryUserDAO();
        users.clearUsers();
    }
    public static void clearGames() {
        var games = new MemoryGameDAO();
        games.clearGames();
    }

}
