package handler;

import spark.*;
import service.AdminService;

public class AdminHandler {
    public static Object clear(Request req, Response res) {
        AdminService.clear();
        return "";
    }
}
