import chess.*;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import server.Server;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server s = new Server();
        try {
            DatabaseManager.initialize();
        } catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
        s.run(8080);
    }
}