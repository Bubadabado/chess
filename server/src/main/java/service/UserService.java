package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class UserService {
    public static RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        var users = new SQLUserDAO();//MemoryUserDAO();
        var auths = new SQLAuthDAO();//MemoryAuthDAO();
        try {
            if(users.getUser(registerRequest.username()) == null) {
                var hashedPwd = BCrypt.hashpw(registerRequest.password(), BCrypt.gensalt());
                users.createUser(new UserData(registerRequest.username(),
                        hashedPwd,
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
        var users = new SQLUserDAO();//MemoryUserDAO();
        var auths = new SQLAuthDAO();//MemoryAuthDAO();
        try {
            if(users.getUser(loginRequest.username()) != null
                    && BCrypt.checkpw(loginRequest.password(), users.getUser(loginRequest.username()).password())) {
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
        var auths = new SQLAuthDAO();//MemoryAuthDAO();
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
    private static boolean checkAuth(String authToken) throws DataAccessException {
        var auths = new SQLAuthDAO();//MemoryAuthDAO();
        return auths.getAuth(authToken) != null;
    }
}
