package handler;

import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.*;
import spark.Request;
import spark.Response;

public class UserHandler {
    public static String handleRegister(Request req, Response res) {
        var serializer = new Gson();
        String data = req.body();
        try {
            var regreq = serializer.fromJson(data, RegisterRequest.class);
            if(regreq.password() == null || regreq.username() == null || regreq.email() == null) {
                res.status(400);
                return serializer.toJson(new ErrorStatus("Error: bad request"));
            }
            var regres = UserService.register(regreq);
            return serializer.toJson(regres);
        } catch (DataAccessException e) {
            res.status(403);
            return serializer.toJson(new ErrorStatus(e.getMessage()));
        }
    }
    public static String handleLogin(Request req, Response res) {
        var serializer = new Gson();
        String data = req.body();
        try {
            var loginreq = serializer.fromJson(data, LoginRequest.class);
            var loginres = UserService.login(loginreq);
            return serializer.toJson(loginres);
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(new ErrorStatus(e.getMessage()));
        }
    }
    public static String handleLogout(Request req, Response res) {
        var serializer = new Gson();
        String data = req.headers("Authorization");
        try {
            var logoutreq = new LogoutRequest(data);
            UserService.logout(logoutreq);
            return "";
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(new ErrorStatus(e.getMessage()));
        }
    }
}
