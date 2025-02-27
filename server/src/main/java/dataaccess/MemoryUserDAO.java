package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Hashtable;

public class MemoryUserDAO implements UserDAO{
    private static Hashtable<String, UserData> users = new Hashtable<>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
       return users.get(username);
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        users.put(userData.username(), userData);
    }

    @Override
    public void clearUsers() {
        users.clear();
    }

    @Override
    public String toString() {
        return users.toString();
    }
}
