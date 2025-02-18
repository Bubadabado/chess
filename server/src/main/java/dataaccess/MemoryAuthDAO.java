package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.Hashtable;

public class MemoryAuthDAO implements AuthDAO {
    private static final Hashtable<String, AuthData> auths = new Hashtable<>();

    @Override
    public void createAuth(AuthData authData) {
        auths.put(authData.username(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) {
        return auths.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) {
        auths.remove(authToken);
    }

    @Override
    public void clearAuths() {
        auths.clear();
    }
}
