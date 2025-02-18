package handler;

import com.google.gson.Gson;
import service.RegisterRequest;
import service.UserService;

public class UserHandler {
    public static String handleRegister(String data) {
        var serializer = new Gson();
        var regreq = serializer.fromJson(data, RegisterRequest.class);
        var regres = UserService.register(regreq);
        return serializer.toJson(regres);
    }
    //TODO: handle login
    //TODO: handle logout
}
