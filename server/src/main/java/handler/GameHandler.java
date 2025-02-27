package handler;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import service.*;
import spark.Request;
import spark.Response;


public class GameHandler {
    public static String handleListGames(String data) {
        var serializer = new Gson();
        System.out.println(data); //TODO remove
        var lgreq = new ListGameRequest(data);//serializer.fromJson(data, ListGameRequest.class);
        var lgres = GameService.listGames(lgreq);
        return serializer.toJson(lgres);
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
    public static String handleJoinGame(String data, String auth) {
        var serializer = new Gson();
        var jgrb = serializer.fromJson(data, JoinGameRequestBody.class);
        var jgreq = new JoinGameRequest(auth, jgrb.playerColor(), jgrb.gameID());
        GameService.joinGame(jgreq);
        //TODO: error handling
        //TODO: caps independence
        return "";
    }
}
