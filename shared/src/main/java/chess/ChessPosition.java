package chess;

import java.util.Objects;

/**
 * Represents a single square position on a chess board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPosition {

    private int row;
    private int col;

    public ChessPosition(int row, int col, boolean zeroIndexed) {
        this.row = (!zeroIndexed) ? row : ChessBoard.BOARD_SIZE - row;
        this.col = (!zeroIndexed) ? col : col + 1;
    }
    public ChessPosition(int row, int col) {
        this(row, col, false);
    }

    /**
     * @return which row this position is in
     * 1 codes for the bottom row
     */
    public int getRow() { return row; }

    /**
     * @return which column this position is in
     * 1 codes for the left column
     */
    public int getColumn() { return col; }

    public void setRow(int row) { this.row = row; }
    public void setCol(int col) { this.col = col; }
    public void setPos(int row, int col) { setRow(row); setCol(col); }

    /**
     * @return a row converted to a properly oriented zero indexed row
     */
    public int getRowConverted() { return ChessBoard.BOARD_SIZE - row; }
    /**
     * @return a properly zero indexed column
     */
    public int getColConverted() { return col - 1; }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPosition that = (ChessPosition) o;
        return row == that.row && col == that.col;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col);
    }
}
