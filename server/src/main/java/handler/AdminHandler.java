package handler;

import spark.*;
import service.AdminService;

public class AdminHandler {
    public static Object clear(Request req, Response res) {
        String auth = req.headers("authorization");
        boolean success = AdminService.clear(auth);
        res.status((success) ? 200 : 401);
//        res.body(req.body());
//        return req.body();
        return "";
    }
}
