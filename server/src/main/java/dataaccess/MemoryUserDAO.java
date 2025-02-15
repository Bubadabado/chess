package dataaccess;

import model.UserData;

public class MemoryUserDAO implements UserDAO{

    @Override
    public UserData getUser(String username) {
        return null; //TODO
    }

    @Override
    public void createUser(UserData userData) {
        //TODO
    }

    @Override
    public void clearUsers() {
        //TODO: clear users
    }
}
