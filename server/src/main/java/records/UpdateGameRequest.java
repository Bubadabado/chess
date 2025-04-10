package records;

import chess.ChessGame;

public record UpdateGameRequest(String authToken, int gameID, ChessGame game) {
}
