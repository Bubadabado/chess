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
//    public LoginResult login(LoginRequest loginRequest) {
//
//    }
    public void logout(LogoutRequest logoutRequest) {}
}
