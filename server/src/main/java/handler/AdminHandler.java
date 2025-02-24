package handler;

import service.AdminService;

public class AdminHandler {
    public static String clear() {
        AdminService.clear();
        return "";
    }
}
