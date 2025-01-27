package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.*;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor currentTeamTurn;

    public ChessGame() {
        board = new ChessBoard();
        currentTeamTurn = TeamColor.WHITE;
        board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(validMoves(move.getStartPosition()).contains(move)) {
            board.addPiece(move.getEndPosition(), new ChessPiece(board.getPiece(move.getStartPosition())));
            board.removePiece(move.getStartPosition());
        } else {
            throw new InvalidMoveException();
        }
    }

    /**
     * Determines if the given team is in check
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, this.board);
    }
    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        return getTeamPositions(otherTeam(teamColor)).stream().anyMatch(position -> {
            return (board.getPiece(position) != null)
                    && ((board.getPiece(position).pieceMoves(board, position)
                    .contains(new ChessMove(position, getKingPosition(teamColor))))
                    || (board.getPiece(position).pieceMoves(board, position)
                    .contains(new ChessMove(position, getKingPosition(teamColor), ChessPiece.PieceType.BISHOP))));
        });
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        return isInCheck(teamColor) && !kingHasValidMoves(teamColor);
    }

    public boolean kingHasValidMoves(TeamColor teamColor) {
        var pos = getKingPosition(teamColor); //TODO: check null
        var moves = board.getPiece(pos).pieceMoves(board, pos);
//        for(var move: moves) {
//
//        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //TODO: handle king moves into check when not in check
        return !isInCheck(teamColor) && getTeamPositions(teamColor).stream().noneMatch(position -> {
            return !board.getPiece(position).pieceMoves(board, position).isEmpty(); //(board.getPiece(position) != null) &&
        });
    }

    /**
     * Gets all piece positions of the given team color
     * @param teamColor color
     * @return all positions of pieces of the color
     */
    private Collection<ChessPosition> getTeamPositions(TeamColor teamColor) {
        Collection<ChessPosition> teamPositions = new ArrayList<>();
        for(int i = 0; i < ChessBoard.BOARD_SIZE; i++) {
            for(int j = 0; j < ChessBoard.BOARD_SIZE; j++) {
                ChessPosition pos = new ChessPosition(i, j, true);
                if(board.getPiece(pos) != null && board.getPiece(pos).getTeamColor() == teamColor) {
                    teamPositions.add(pos);
                }
            }
        }
        return teamPositions;
    }

    /**
     * @param teamColor color
     * @return the king's position
     */
    private ChessPosition getKingPosition(TeamColor teamColor) {
        var positions = getTeamPositions(teamColor);
        for(var position : positions) {
            if(board.getPiece(position).getPieceType() == ChessPiece.PieceType.KING) {
                return position;
            }
        }
        return null;
    }

    /**
     * @param color color
     * @return other team's color
     */
    private TeamColor otherTeam(TeamColor color) {
        return (color == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Sets this game's chessboard with a given board
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
