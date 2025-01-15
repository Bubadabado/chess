package chess;

import javax.swing.text.Position;
import java.util.*;
import java.lang.Math;

import java.io.*;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    //oob checking
    private int outOfBoundsFix(int val) {
        return Math.clamp(val, 0, ChessBoard.BOARD_SIZE - 1);
    }
    private boolean isOutOfBounds(ChessPosition pos) {
        return pos.getRowConverted() < 0 || pos.getRowConverted() >= ChessBoard.BOARD_SIZE || pos.getColConverted() < 0 || pos.getColConverted() >= ChessBoard.BOARD_SIZE;
    }

    /*Individual Piece moves*/
    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition myPosition) {
        int row_begin = outOfBoundsFix(myPosition.getRowConverted() - 1);
        int row_end   = outOfBoundsFix(myPosition.getRowConverted() + 1);
        int col_begin = outOfBoundsFix(myPosition.getColConverted() - 1);
        int col_end   = outOfBoundsFix(myPosition.getColConverted() + 1);
        Collection<ChessMove> moves = new ArrayList<>();
        for(var i = row_begin; i <= row_end; i++) {
            for (var j = col_begin; j <= col_end; j++) {
                var pos = new ChessPosition(i, j, true);
                var target = board.getPiece(pos);
                if (validMove(target) || validAttack(target)) {
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
        Collection<ChessMove> moves = new ArrayList<>();
        BiConsumer<Integer, Integer> addMove = (rowOff, colOff) ->
                moves.addAll(bishopMovesHelper(board, myPosition, myPosition,
                new ChessPosition(myPosition.getRowConverted() - rowOff, myPosition.getColConverted() - colOff, true)));
        addMove.accept(-1, -1);
        addMove.accept(-1, 1);
        addMove.accept(1, 1);
        addMove.accept(1, -1);
        return moves;
    }
    private Collection<ChessMove> bishopMovesHelper(ChessBoard board, ChessPosition startPosition, ChessPosition previousPosition, ChessPosition currentPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        if(!isOutOfBounds(currentPosition)) {
            var target = board.getPiece(currentPosition);
            if (validAttack(target)) {
                moves.add(new ChessMove(startPosition, currentPosition));
            } else if (validMove(target)) {
                var newPosition = new ChessPosition(currentPosition.getRowConverted() - previousPosition.getRowConverted(),
                        currentPosition.getColConverted() - previousPosition.getColConverted(), true); //offset new position
                moves.addAll(bishopMovesHelper(board, startPosition, currentPosition, newPosition));
            }
        }
        return moves;
    }
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        throw new RuntimeException("Not implemented");
    }
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        BiFunction<Integer, Integer, Collection<ChessPosition>> checkPosition = (roff, coff) ->
                incrementalPositionCheck(board, myPosition, roff, coff);
        //find the available positions to move to
        List<ChessPosition> positionList = Stream.of(
                checkPosition.apply(0, 1),
                checkPosition.apply(0, -1),
                checkPosition.apply(1, 0),
                checkPosition.apply(-1, 0))
                    .flatMap(Collection::stream).collect(Collectors.toList());
        return getMovesFromPositions(myPosition, positionList); //convert positions to moves
    }

    /**
     * Recursively checks the board at positions incremented by respective row and column offsets
     * @param board
     * @param myPosition
     * @param roff
     * @param coff
     * @return
     */
    private Collection<ChessPosition> incrementalPositionCheck(ChessBoard board, ChessPosition myPosition, int roff, int coff) {
        int row = myPosition.getRowConverted() + roff;
        int col = myPosition.getColConverted() + coff;
        var newPos = new ChessPosition(row, col, true);
        if(isOutOfBounds(newPos)) { return new ArrayList<>(); }
        var target = board.getPiece(newPos);
        Collection<ChessPosition> positions = ((validAttack(target) || validMove(target))) ? new ArrayList<>(List.of(newPos)) : new ArrayList<>();
        if(validMove(target)) {
            positions.addAll(incrementalPositionCheck(board, newPos, roff, coff));
        }
        System.out.println(positions);
        return positions;
    }

    private Collection<ChessMove> getMovesFromPositions(ChessPosition start, Collection<ChessPosition> positions) {
        Collection<ChessMove> moves = new ArrayList<>();
        positions.forEach((pos) -> moves.add(new ChessMove(start, pos)));
        return moves;
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        //TODO: dependant on team
        int i = outOfBoundsFix(myPosition.getRowConverted() - 1);
        int j = outOfBoundsFix(myPosition.getColConverted() - 1);
        var pos = new ChessPosition(i, j, true);
        var target = board.getPiece(pos);
        if(validAttack(target)) {
            moves.add(new ChessMove(myPosition, pos));
        }
        pos.setCol(pos.getColConverted() + 1);
        if(validMove(board.getPiece(pos))) {
            moves.add(new ChessMove(myPosition, pos));
        }
        pos.setCol(pos.getColConverted() + 1);
        if(validAttack(target)) {
            moves.add(new ChessMove(myPosition, pos));
        }
        return moves;
    }

    /**
     * Check move validity; semantic functions
     * @param target
     * @return
     */
    private boolean validMove(ChessPiece target) {
        return target == null;
    }
    private boolean validAttack(ChessPiece target) {
        return target != null && target.pieceColor != this.pieceColor;
    }
}
