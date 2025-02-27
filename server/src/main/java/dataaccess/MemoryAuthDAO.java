package dataaccess;

import model.AuthData;
import model.GameData;

import java.util.Hashtable;

public class MemoryAuthDAO implements AuthDAO {
    private static final Hashtable<String, AuthData> auths = new Hashtable<>();

    @Override
    public void createAuth(AuthData authData) {
        auths.put(authData.authToken(), authData);
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        return auths.get(authToken);
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        auths.remove(authToken);
    }

    @Override
    public void clearAuths() {
        auths.clear();
    }

    @Override
    public String toString() {
        return auths.toString();
    }
}
