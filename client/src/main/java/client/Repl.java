package client;

import java.util.Scanner;

import chess.ChessGame;
import ui.EscapeSequences;
import websocket.messages.Notification;

public class Repl implements NotificationHandler {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl, this);
    }

    public void run() {
        System.out.println("How about a nice game of chess? Log in to begin.");
        System.out.print(client.help());

        Scanner scanner = new Scanner(System.in);
        var result = "";
        while (!result.equals("quit")) {
            printInputPrompt();
            try {
                result = client.handleInput(scanner.nextLine());
                System.out.print(EscapeSequences.SET_TEXT_COLOR_BLUE + result);
            } catch (Throwable e) {
                System.out.print(e.toString());
            }
        }
        System.out.println();
    }

    public void notify(String n) {
        System.out.println(n);
        printInputPrompt();
    }
    public void reload(ChessGame g) {
        client.reloadGame(g);
    }


    private void printInputPrompt() {
        System.out.print("\n" + EscapeSequences.SET_TEXT_COLOR_RED
                + ((client.getInGameState())
                    ? "[In game]"
                    : ((client.getLoginState())
                        ? "[Logged in] "
                        : "[Logged out]"))
                + ">>> " + EscapeSequences.SET_TEXT_COLOR_GREEN);
    }
}
