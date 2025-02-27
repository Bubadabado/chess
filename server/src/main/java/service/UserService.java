package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    public static RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        var users = new MemoryUserDAO();
        var auths = new MemoryAuthDAO();
        try {
            if(users.getUser(registerRequest.username()) == null) {
                users.createUser(new UserData(registerRequest.username(),
                        registerRequest.password(),
                        registerRequest.email()));
                var authData = new AuthData(AuthGen.generateAuthToken(), registerRequest.username());
                auths.createAuth(authData);
                return new RegisterResult(authData.username(), authData.authToken());
            } else {
                throw new DataAccessException("Error: already taken");
            }
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public static LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        var users = new MemoryUserDAO();
        var auths = new MemoryAuthDAO();
        try {
            if(users.getUser(loginRequest.username()) != null && loginRequest.password().equals(users.getUser(loginRequest.username()).password())) {
                var authData = new AuthData(AuthGen.generateAuthToken(), loginRequest.username());
                auths.createAuth(authData);
                return new LoginResult(authData.username(), authData.authToken());
            } else {
                throw new DataAccessException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw e;
        }
    }
    public static void logout(LogoutRequest logoutRequest) throws DataAccessException {
        var auths = new MemoryAuthDAO();
        try {
            if(logoutRequest.authToken() != null && checkAuth(logoutRequest.authToken())) {
                auths.deleteAuth(logoutRequest.authToken());
            } else {
                throw new DataAccessException("Error: unauthorized");
            }
        } catch (DataAccessException e) {
            throw e;
        }

    }
    private static boolean checkAuth(String authToken) {
        var auths = new MemoryAuthDAO();
        System.out.println(auths);
        return auths.getAuth(authToken) != null;
    }
}
