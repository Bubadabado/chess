package client;

import server.ServerFacade;

public class ChessClient {
    private final ServerFacade server;
    private final String serverUrl;
    private boolean isLoggedIn;

    public ChessClient(String serverUrl) {
        server = new ServerFacade(serverUrl);
        this.serverUrl = serverUrl;
        isLoggedIn = false;
    }

    public String help() {
        return ((isLoggedIn)
                ? """
                    - register <username> <password> <email>
                    - login <username> <password>
                    - quit
                    - help
                    """
                : """
                    - createGame <name>
                    - list
                    - join <id> [WHITE|BLACK]
                    - observe <id>
                    - logout
                    - help
                    """
        );
    }
}
