package client;

import java.util.Scanner;

public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
    }

    public void run() {
        System.out.println("How about a nice game of chess? Sign in to begin.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
    }
}
