package server;

import handler.AdminHandler;
import handler.GameHandler;
import handler.UserHandler;
import spark.*;

public class Server {

    public int run(int desiredPort) {
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
        Spark.delete("/db", (req, res) -> AdminHandler.clear());
        Spark.post("/user", (req, res) -> UserHandler.handleRegister(req.body()));
        Spark.post("/session", (req, res) -> UserHandler.handleLogin(req.body()));
        Spark.delete("/session", (req, res) -> UserHandler.handleLogout(req.headers("authorization")));
        Spark.get("/game", (req, res) -> GameHandler.handleListGames(req.headers("authorization")));
        Spark.post("/game", (req, res) -> GameHandler.handleCreateGame(req.body(), req.headers("authorization")));
        Spark.put("/game", (req, res) -> GameHandler.handleJoinGame(req.body(), req.headers("authorization")));
    }
}
