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
        Spark.delete("/db", AdminHandler::clear);
        Spark.post("/user", UserHandler::handleRegister);
        Spark.post("/session", UserHandler::handleLogin);
        Spark.delete("/session", UserHandler::handleLogout);
//        Spark.get("/game", (req, res) -> GameHandler.handleListGames(req.headers("authorization")));
//        Spark.post("/game", (req, res) -> GameHandler.handleCreateGame(req.body(), req.headers("authorization")));
//        Spark.put("/game", (req, res) -> GameHandler.handleJoinGame(req.body(), req.headers("authorization")));
    }
}
