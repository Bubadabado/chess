package dataaccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Hashtable;

public class MemoryUserDAO implements UserDAO{
    private static Hashtable<String, UserData> users;

    @Override
    public UserData getUser(String username) {
       return users.get(username);
    }

    @Override
    public void createUser(UserData userData) {
        users.put(userData.username(), userData);
    }

    @Override
    public void clearUsers() {
        users.clear();
    }
}
