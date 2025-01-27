package chess;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.lang.Iterable;

import static java.util.Arrays.asList;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private static final ChessPiece.PieceType[] BACK_ROW_ORDER = {
        ChessPiece.PieceType.ROOK, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.BISHOP,
        ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.KING,
        ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK
    };
    public static final int BOARD_SIZE = 8;
    private ChessPiece[][] board;

    public ChessBoard() {
        this.board = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
    }

    /**
     * Adds a chess piece to the chessboard
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRowConverted()][position.getColConverted()] = piece;//new ChessPiece(piece);
    }

    /**
     * removes a chesspiece
     * @param position the position to remove
     */
    public void removePiece(ChessPosition position) {
        addPiece(position, null);
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRowConverted()][position.getColConverted()];
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        addBackRow(0, ChessGame.TeamColor.BLACK);
        addPawnRow(1, ChessGame.TeamColor.BLACK);
        addPawnRow(BOARD_SIZE - 2, ChessGame.TeamColor.WHITE);
        addBackRow(BOARD_SIZE - 1, ChessGame.TeamColor.WHITE);
    }

    /**
     * Adds a row of pawns
     * @param row
     * @param color
     */
    private void addPawnRow(int row, ChessGame.TeamColor color) {
        for(int col = 0; col < BOARD_SIZE; col++) {
            addPiece(new ChessPosition(row, col, true), new ChessPiece(color, ChessPiece.PieceType.PAWN));
        }
    }

    /**
     * Adds a back row
     * @param row
     * @param color
     */
    private void addBackRow(int row, ChessGame.TeamColor color) {
        BiConsumer<Integer, ChessPiece.PieceType> addBackRowPiece = (col, type) ->
                addPiece(new ChessPosition(row, col, true), new ChessPiece(color, type));
        int[] col = {0}; //used like a pointer to allow index incrementing within lambda function
        asList(BACK_ROW_ORDER).forEach(piece -> addBackRowPiece.accept(col[0]++, piece));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }
}
