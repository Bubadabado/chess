package service;

import dataaccess.*;

public class AdminService {
    public static boolean clear() {
        clearAuths();
        clearUsers();
        clearGames();
        return true;
    }
    private static void clearAuths() {
        var auths = new SQLAuthDAO();//MemoryAuthDAO();
        auths.clearAuths();
    }
    private static void clearUsers() {
        var users = new SQLUserDAO();//MemoryUserDAO();
        users.clearUsers();
    }
    private static void clearGames() {
        var games = new MemoryGameDAO();
        games.clearGames();
    }
}
