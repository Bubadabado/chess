package handler;
import com.google.gson.Gson;
import service.CreateGameRequest;
import service.GameService;
import service.ListGameRequest;


public class GameHandler {
    public static String handleListGames(String data) {
        var serializer = new Gson();
        var lgreq = serializer.fromJson(data, ListGameRequest.class);
        var lgres = GameService.listGames(lgreq);
        return serializer.toJson(lgres);
    }
    public static String handleCreateGame(String data) {
        var serializer = new Gson();
        var cgreq = serializer.fromJson(data, CreateGameRequest.class);
        var cgres = GameService.createGame(cgreq);
        return serializer.toJson(cgres);
    }
}
