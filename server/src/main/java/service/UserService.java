package service;

import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryUserDAO;
import model.AuthData;
import model.UserData;

public class UserService {
    public RegisterResult register(RegisterRequest registerRequest) {
//        try {
        var users = new MemoryUserDAO();
        var auths = new MemoryAuthDAO();
        if(users.getUser(registerRequest.username()) == null) {
            users.createUser(new UserData(registerRequest.username(),
                    registerRequest.password(),
                    registerRequest.email()));
            var authData = new AuthData(AuthGen.generateAuthToken(), registerRequest.username());
            auths.createAuth(authData);
            return new RegisterResult(authData.username(), authData.authToken());
        } else {
            return new RegisterResult("", "");
        }
//        } catch (DataAccessException e) {
//
//        }
    }
    public LoginResult login(LoginRequest loginRequest) {
        var users = new MemoryUserDAO();
        var auths = new MemoryAuthDAO();
        if(loginRequest.password().equals(users.getUser(loginRequest.username()).password())) {
            var authData = new AuthData(AuthGen.generateAuthToken(), loginRequest.username());
            auths.createAuth(authData);
            return new LoginResult(authData.username(), authData.authToken());
        } else {
            return new LoginResult("", "");
        }
    }
    public void logout(LogoutRequest logoutRequest) {
        var auths = new MemoryAuthDAO();
        auths.deleteAuth(logoutRequest.authToken());
    }
}
