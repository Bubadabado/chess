package chess;

import java.util.Arrays;
import java.util.Objects;
import java.util.function.BiConsumer;

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
    public ChessBoard(ChessBoard other) {
        this.board = new ChessPiece[BOARD_SIZE][BOARD_SIZE];
        for(int row = 0; row < BOARD_SIZE; row++) {
            for(int col = 0; col < BOARD_SIZE; col++) {
                var pos = new ChessPosition(row, col, true);
                var piece = other.getPiece(pos);
                if(piece != null) {
                    this.addPiece(pos, new ChessPiece(piece));
                }
            }
        }
    }

    /**
     * Adds a chess piece to the chessboard
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        board[position.getRowConverted()][position.getColConverted()] = piece;
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

    /**
     * Check OOB
     * @param pos position
     * @return whether the given position is out of bounds
     */
    public static boolean isOutOfBounds(ChessPosition pos) {
        return pos.getRowConverted() < 0
                || pos.getRowConverted() >= ChessBoard.BOARD_SIZE
                || pos.getColConverted() < 0
                || pos.getColConverted() >= ChessBoard.BOARD_SIZE;
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

    /**
     * Print and format the board
     * @param team
     * @return
     */
    public String toString(ChessGame.TeamColor team) {
        StringBuilder boardString = new StringBuilder();
        boardString.append(printRowHeader(team));
        for(int row = 0; row < BOARD_SIZE; row++) {
            boardString.append(printRow(((team == ChessGame.TeamColor.WHITE)
                    ? row : BOARD_SIZE - 1 - row), team));
        }
        boardString.append(printRowHeader(team));
        return boardString.toString();
    }
    private String printRowHeader(ChessGame.TeamColor team) {
        StringBuilder rowString = new StringBuilder();
        rowString.append(EscapeSequencesShared.SET_BG_COLOR_YELLOW);
        rowString.append(EscapeSequencesShared.SET_TEXT_COLOR_BLACK);
        rowString.append("   ");
        for(int row = 0; row < BOARD_SIZE; row++) {
            rowString.append(" ");
            rowString.append((team == ChessGame.TeamColor.WHITE)
                    ? (char)('a' + row)
                    : (char)('h' - row)
            );
            rowString.append(" ");
        }
        rowString.append("   ");
        rowString.append(EscapeSequencesShared.RESET_BG_COLOR);
        rowString.append("\n");
        return rowString.toString();
    }
    private String printRow(int row, ChessGame.TeamColor team) {
        StringBuilder rowString = new StringBuilder();
        rowString.append(printRowCount(row));
        for(int col = 0; col < BOARD_SIZE; col++) {
            rowString.append(setBgColor(team, row, col));
            var pos = new ChessPosition(row, ((team == ChessGame.TeamColor.WHITE)
                ? col : BOARD_SIZE - 1 - col), true);
            rowString.append(getPieceString(getPiece(pos)));
        }
        rowString.append(printRowCount(row));
        rowString.append(EscapeSequencesShared.RESET_BG_COLOR);
        rowString.append("\n");
        return rowString.toString();
    }
    private String printRowCount(int row) {
        return EscapeSequencesShared.SET_BG_COLOR_YELLOW
                + " " + EscapeSequencesShared.SET_TEXT_COLOR_BLACK + (BOARD_SIZE - row) + " ";
    }
    private String setBgColor(ChessGame.TeamColor team, int row, int col) {
        return ((team == ChessGame.TeamColor.WHITE)
                ? ((row % 2 == col % 2)
                    ? EscapeSequencesShared.SET_BG_COLOR_RED
                    : EscapeSequencesShared.SET_BG_COLOR_BLACK)
                : ((row % 2 == col % 2)
                    ? EscapeSequencesShared.SET_BG_COLOR_BLACK
                    : EscapeSequencesShared.SET_BG_COLOR_RED)
        );
    }
    private String getPieceString(ChessPiece piece) {
        if (piece == null) { return "   "; }
        var type = piece.getPieceType();
        return ((piece.getTeamColor() == ChessGame.TeamColor.WHITE)
                ? EscapeSequencesShared.SET_TEXT_COLOR_WHITE
                : EscapeSequencesShared.SET_TEXT_COLOR_BLUE)
        + switch (type) {
            case ChessPiece.PieceType.KING -> " K ";
            case QUEEN -> " Q ";
            case BISHOP -> " B ";
            case KNIGHT -> " N ";
            case ROOK -> " R ";
            case PAWN -> " P ";
        };
    }
}
