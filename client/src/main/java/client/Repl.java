package client;

import java.util.Scanner;
import ui.EscapeSequences;

public class Repl {
    private final ChessClient client;

    public Repl(String serverUrl) {
        client = new ChessClient(serverUrl);
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
