package server;

import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import handler.AdminHandler;
import handler.GameHandler;
import handler.UserHandler;
import spark.*;
import websocket.WebSocketHandler;

public class Server {
    private static final WebSocketHandler webSocketHandler = new WebSocketHandler();

//    public Server() {
//        webSocketHandler = new WebSocketHandler();
//    }

    public int run(int desiredPort) {
        try {
            DatabaseManager.initialize();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        //This line initializes the server and can be removed once you have a functioning endpoint 
//        Spark.init();
        createRoutes();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }

    private static void createRoutes() {
        Spark.webSocket("/ws", webSocketHandler);
        Spark.delete("/db", AdminHandler::clear);
        Spark.post("/user", UserHandler::handleRegister);
        Spark.post("/session", UserHandler::handleLogin);
        Spark.delete("/session", UserHandler::handleLogout);
        Spark.get("/game", GameHandler::handleListGames);
        Spark.post("/game", GameHandler::handleCreateGame);
        Spark.put("/game", GameHandler::handleJoinGame);
    }
}
