package handler;

import com.google.gson.Gson;
import service.LoginRequest;
import service.RegisterRequest;
import service.UserService;

public class UserHandler {
    public static String handleRegister(String data) {
        var serializer = new Gson();
        var regreq = serializer.fromJson(data, RegisterRequest.class);
        var regres = UserService.register(regreq);
        return serializer.toJson(regres);
    }
    public static String handleLogin(String data) {
        var serializer = new Gson();
        var loginreq = serializer.fromJson(data, LoginRequest.class);
        var loginres = UserService.login(loginreq);
        return serializer.toJson(loginres);
    }
    //TODO: handle login
    //TODO: handle logout
}
