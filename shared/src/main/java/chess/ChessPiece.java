package chess;

import java.util.*;
import java.util.function.BiFunction;

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
        int rowBegin = myPosition.getRowConverted() - 1;
        int rowEnd   = myPosition.getRowConverted() + 1;
        int colBegin = myPosition.getColConverted() - 1;
        int colEnd   = myPosition.getColConverted() + 1;
        Collection<ChessMove> moves = new ArrayList<>();
        for(var i = rowBegin; i <= rowEnd; i++) {
            for (var j = colBegin; j <= colEnd; j++) {
                var pos = new ChessPosition(i, j, true);
                if(isOutOfBounds(pos)) { continue; }
                var target = board.getPiece(pos);
                if (validMove(target) || validAttack(target)) {
                    moves.add(new ChessMove(myPosition, pos));
                }
            }
        }
        return moves;
    }
    private Collection<ChessMove> queenMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = rookMoves(board, myPosition);
        moves.addAll(bishopMoves(board, myPosition));
        return moves;
    }
    private Collection<ChessMove> bishopMoves(ChessBoard board, ChessPosition myPosition) {
        List<int[]> offsets = Arrays.asList(
                new int[]{1, 1},
                new int[]{1, -1},
                new int[]{-1, 1},
                new int[]{-1, -1}
        );
        return createMoveList(board, myPosition, offsets);
    }
    private Collection<ChessMove> knightMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        for(int i = 0; i < 8; i++) {
            //use modulus calculations to determine the knight's move combination
            int row = (i % 2) + 1;              //1 or 2
            int col = ((i + 1) % 2) + 1;        //2 or 1
            int rsign = (1 - 2 * ((i / 2) % 2));//alternating pattern (1, -1)
            int csign = (1 - 2 * ((i / 4) % 2));//double alternating pattern (1, 1, -1, -1)
            var pos = new ChessPosition(
                    myPosition.getRowConverted() + (row * rsign),
                    myPosition.getColConverted() + (col * csign), true);
            if(!isOutOfBounds(pos)) {
                var target = board.getPiece(pos);
                if(validMove(target) || validAttack(target)) {
                    moves.add(new ChessMove(myPosition, pos));
                }
            }
        }
        return moves;
    }
    private Collection<ChessMove> rookMoves(ChessBoard board, ChessPosition myPosition) {
        List<int[]> offsets = Arrays.asList(
                new int[]{0, 1},
                new int[]{0, -1},
                new int[]{1, 0},
                new int[]{-1, 0}
        );
        return createMoveList(board, myPosition, offsets);
    }
    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> moves = new ArrayList<>();
        int i = myPosition.getRowConverted() + ((this.pieceColor == ChessGame.TeamColor.WHITE) ? -1 : 1);
        int j = myPosition.getColConverted() - 1;
        var pos = new ChessPosition(i, j, true);
        //attack
        moves.addAll(validatePawnAttack(board, myPosition, pos));
        pos = new ChessPosition(i, j + 1, true);
        //single move
        if(validMove(pos, board)) {
            if(pos.getRowConverted() == 0 || pos.getRowConverted() == ChessBoard.BOARD_SIZE - 1) {
                moves.addAll(createPromotions(myPosition, pos));
            }
            else {
                moves.add(new ChessMove(myPosition, pos));
                //double move
                if((myPosition.getRowConverted() == 1 && this.pieceColor == ChessGame.TeamColor.BLACK) ||
                        (myPosition.getRowConverted() == ChessBoard.BOARD_SIZE - 2 && this.pieceColor == ChessGame.TeamColor.WHITE)) {
                    pos = new ChessPosition(i + ((this.pieceColor == ChessGame.TeamColor.WHITE) ? -1 : 1), j + 1, true);
                    if(validMove(pos, board)) {
                        moves.add(new ChessMove(myPosition, pos));
                    }
                }
            }
        }
        //attack
        pos = new ChessPosition(i, j + 2, true);
        moves.addAll(validatePawnAttack(board, myPosition, pos));
        return moves;
    }
    private Collection<ChessMove> validatePawnAttack(ChessBoard board, ChessPosition myPosition, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<>();
        if(validAttack(pos, board)) {
            if(pos.getRowConverted() == 0 || pos.getRowConverted() == ChessBoard.BOARD_SIZE - 1) {
                moves.addAll(createPromotions(myPosition, pos));
            }
            else {
                moves.add(new ChessMove(myPosition, pos));
            }
        }
        return moves;
    }
    private Collection<ChessMove> createPromotions(ChessPosition myPosition, ChessPosition pos) {
        Collection<ChessMove> moves = new ArrayList<>();
        moves.add(new ChessMove(myPosition, pos, PieceType.ROOK));
        moves.add(new ChessMove(myPosition, pos, PieceType.KNIGHT));
        moves.add(new ChessMove(myPosition, pos, PieceType.BISHOP));
        moves.add(new ChessMove(myPosition, pos, PieceType.QUEEN));
        return moves;
    }

    /**
     * TODO: supply a bifunction parameter to allow createMoveList support for more modular piece types
     * Creates a list of moves valid move sgiven a list of offset tuples
     * @param board game board
     * @param myPosition start position
     * @param offsets list of offset tuples acting like vectors to crawl the board
     * @return a list of moves
     */
    private Collection<ChessMove> createMoveList(ChessBoard board, ChessPosition myPosition, List<int[]> offsets) {
        BiFunction<Integer, Integer, Collection<ChessPosition>> checkPosition = (roff, coff) ->
                incrementalPositionCheck(board, myPosition, roff, coff);
        List<ChessPosition> positionList = offsets.stream()
                .map(offset -> checkPosition.apply(offset[0], offset[1]))
                .flatMap(Collection::stream).toList();
        return getMovesFromPositions(myPosition, positionList); //convert positions to moves
    }

    /**
     * Recursively checks the board at positions incremented by respective row and column offsets; used for pieces with 'infinite' movement
     * @param board game board
     * @param myPosition current position
     * @param roff row offset
     * @param coff column offset
     * @return a collection of positions
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
        return positions;
    }

    /**
     * creates a collection of moves given a collection of positions
     * @param start position
     * @param positions - a list of all valid move positions
     * @return moves
     */
    private Collection<ChessMove> getMovesFromPositions(ChessPosition start, Collection<ChessPosition> positions) {
        Collection<ChessMove> moves = new ArrayList<>();
        positions.forEach((pos) -> moves.add(new ChessMove(start, pos)));
        return moves;
    }

    /**
     * Check move validity; semantic functions, distinguished for the sake of the pawn's special movement options
     * @param target piece
     */
    private boolean validMove(ChessPiece target) {
        return target == null;
    }
    private boolean validMove(ChessPosition pos, ChessBoard board) {
        if(isOutOfBounds(pos)) {
            return false;
        } else {
            ChessPiece target = board.getPiece(pos);
            return validMove(target);
        }
    }
    private boolean validAttack(ChessPiece target) {
        return target != null && target.pieceColor != this.pieceColor;
    }
    private boolean validAttack(ChessPosition pos, ChessBoard board) {
        if(isOutOfBounds(pos)) {
            return false;
        } else {
            ChessPiece target = board.getPiece(pos);
            return validAttack(target);
        }
    }

    /**
     * Check OOB
     * @param pos position
     * @return whether the given position is out of bounds
     */
    private boolean isOutOfBounds(ChessPosition pos) {
        return pos.getRowConverted() < 0
                || pos.getRowConverted() >= ChessBoard.BOARD_SIZE
                || pos.getColConverted() < 0
                || pos.getColConverted() >= ChessBoard.BOARD_SIZE;
    }
}
