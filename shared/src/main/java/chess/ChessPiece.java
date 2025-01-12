package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.lang.Math;
import java.util.Objects;

import java.io.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private ChessGame.TeamColor pieceColor;
    private ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }
    public ChessPiece(ChessPiece other) {
        this(other.getTeamColor(), other.getPieceType());
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (type) {
            case PieceType.KING   -> kingMoves  (board, myPosition);
            case PieceType.QUEEN  -> queenMoves (board, myPosition);
            case PieceType.BISHOP -> bishopMoves(board, myPosition);
            case PieceType.KNIGHT -> knightMoves(board, myPosition);
            case PieceType.ROOK   -> rookMoves  (board, myPosition);
            case PieceType.PAWN   -> pawnMoves  (board, myPosition);
        };
    }

    /*Individual Piece moves*/
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        int row_begin = Math.max(myPosition.getRowConverted() - 1, 0);
        int row_end   = Math.min(myPosition.getRowConverted() + 1, ChessBoard.BOARD_SIZE - 1);
        int col_begin = Math.max(myPosition.getColConverted() - 1, 0);
        int col_end   = Math.min(myPosition.getColConverted() + 1, ChessBoard.BOARD_SIZE - 1);
        Collection<ChessMove> moves = new ArrayList<>();
        for(var i = row_begin; i <= row_end; i++) {
            for (var j = col_begin; j <= col_end; j++) {
                var pos = new ChessPosition(i, j, true);
                var target = board.getPiece(pos);
                if (target == null || target.getTeamColor() != this.pieceColor) {
                    moves.add(new ChessMove(myPosition, pos));
                }
            }
        }
        return moves;
    }
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
}
