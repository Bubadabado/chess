package websocket.commands;

public class ConnectCommand extends UserGameCommand {
    private final String color;
    private final String userName;

    public ConnectCommand(String authToken, Integer gameID, String color, String userName) {
        super(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        this.color = color;
        this.userName = userName;
    }

    public String getColor() { return this.color; }
    public String getUserName() { return this.userName; }
}
