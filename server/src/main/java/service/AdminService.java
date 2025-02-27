package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;

public class AdminService {
    public static boolean clear(String authToken) {
//        if(authToken != null && checkAuth(authToken)) {
            clearAuths();
            clearUsers();
            clearGames();
            return true;
//        } else {
//            return false;
//        }
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

    private static boolean checkAuth(String authToken) {
        var auths = new MemoryAuthDAO();
        return auths.getAuth(authToken) != null;
    }

}
