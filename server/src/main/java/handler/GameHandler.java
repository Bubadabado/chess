package handler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.*;
import spark.Request;
import spark.Response;

import java.util.Locale;


public class GameHandler {
    public static String handleListGames(Request req, Response res) {
        String data = req.headers("authorization");
        var serializer = new Gson();
        try {
            var lgreq = new ListGameRequest(data);
            var lgres = GameService.listGames(lgreq);
            System.out.println(serializer.toJson(lgres));
            return serializer.toJson(lgres);
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(new ErrorStatus(e.getMessage()));
        }
    }
    public static String handleCreateGame(Request req, Response res) {
        var serializer = new Gson();
        String data = req.body();
        String auth = req.headers("authorization");
        try {
            var gameName = serializer.fromJson(data, GameName.class);
            var cgreq = new CreateGameRequest(auth, gameName.gameName());
            if(cgreq.gameName() == null || cgreq.authToken() == null) {
                res.status(400);
                return serializer.toJson(new ErrorStatus("Error: bad request"));
            }
            var cgres = GameService.createGame(cgreq);
            return serializer.toJson(cgres);
        } catch (DataAccessException e) {
            res.status(401);
            return serializer.toJson(new ErrorStatus(e.getMessage()));
        }
    }
    public static String handleJoinGame(Request req, Response res) {
        var serializer = new Gson();
        String data = req.body();
        String auth = req.headers("authorization");
        try {
            var jgrb = serializer.fromJson(data, JoinGameRequestBody.class);
            if(auth == null
                    || jgrb.playerColor() == null
                    || !isValidPlayerColor(jgrb.playerColor().toLowerCase())
                    || jgrb.gameID() < 1) {
                res.status(400);
                return serializer.toJson(new ErrorStatus("Error: bad request"));
            }
            var jgreq = new JoinGameRequest(auth, jgrb.playerColor().toLowerCase(), jgrb.gameID());
            GameService.joinGame(jgreq);
            return "";
        } catch (DataAccessException e) {
            res.status(((e.getMessage().equals("Error: unauthorized")) ? 401 : 403));
            return serializer.toJson(new ErrorStatus(e.getMessage()));
        }
    }
    private static boolean isValidPlayerColor(String playerColor) {
        return playerColor.equals("black") || playerColor.equals("white");
    }
}
