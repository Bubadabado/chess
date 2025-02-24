package handler;
import com.google.gson.Gson;
import service.*;


public class GameHandler {
    public static String handleListGames(String data) {
        var serializer = new Gson();
        System.out.println(data); //TODO remove
        var lgreq = new ListGameRequest(data);//serializer.fromJson(data, ListGameRequest.class);
        var lgres = GameService.listGames(lgreq);
        return serializer.toJson(lgres);
    }
    public static String handleCreateGame(String data, String auth) {
        var serializer = new Gson();
        System.out.println(data); //TODO remove
        var gameName = serializer.fromJson(data, GameName.class);
        var cgreq = new CreateGameRequest(auth, gameName.gameName());
        System.out.println(cgreq);
        var cgres = GameService.createGame(cgreq);
        return serializer.toJson(cgres);
    }
    public static String handleJoinGame(String data, String auth) {
        var serializer = new Gson();
        var jgrb = serializer.fromJson(data, joinGameRequestBody.class);
        var jgreq = new JoinGameRequest(auth, jgrb.playerColor(), jgrb.gameID());
        GameService.joinGame(jgreq);
        //TODO: error handling
        //TODO: caps independence
        return "";
    }
}
