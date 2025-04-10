package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Set;
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
    private boolean gameOver;

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
        return validMoves(startPosition, board);
    }
    private Collection<ChessMove> validMoves(ChessPosition startPosition, ChessBoard board) {
        var target = board.getPiece(startPosition);
        var newMoves = new ArrayList<ChessMove>();
        if(ChessBoard.isOutOfBounds(startPosition) || target == null) {
            return newMoves;
        }
        var moves = target.pieceMoves(board, startPosition);
        for(var move: moves) {
            var newBoard = new ChessBoard(board);
            makeValidMove(move, newBoard);
            if(!isInCheck(target.getTeamColor(), newBoard)) {
                newMoves.add(move);
            }
        }
        return newMoves;
    }

    /**
     * @param color color of the team to check
     * @return a list of positions threatened by the given team
     */
    private Collection<ChessPosition> threatenedTiles(TeamColor color, ChessBoard board) {
        var piecePositions = getTeamPositions(color);
        var tiles = new ArrayList<ChessPosition>();
        for(var pos: piecePositions) {
            var target = board.getPiece(pos);
            tiles.addAll(target.pieceMoves(board, pos)
                    .stream()
                    .map(ChessMove::getEndPosition)
                    .toList());
        }
        return tiles;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        if(board.getPiece(move.getStartPosition()) != null
                && board.getPiece(move.getStartPosition()).getTeamColor() == currentTeamTurn
                && validMoves(move.getStartPosition()).contains(move)) {
            makeValidMove(move, board);
            currentTeamTurn = otherTeam(currentTeamTurn);
        } else {
            throw new InvalidMoveException();
        }
    }
    /**
     * Makes a move, assuming that it is already valid.
     * @param move move
     * @param board board
     */
    private void makeValidMove(ChessMove move, ChessBoard board) {
        var target = board.getPiece(move.getStartPosition());
        board.addPiece(move.getEndPosition(), new ChessPiece(
                target.getTeamColor(),
                (move.getPromotionPiece() == null) ? target.getPieceType() : move.getPromotionPiece()
        ));
        board.removePiece(move.getStartPosition());
    }

    /**
     * Determines if the given team is in check
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, this.board);
    }
    private boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        return threatenedTiles(otherTeam(teamColor), board).contains(getKingPosition(teamColor, board));
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        var threatsToKing = threateningPieces(otherTeam(teamColor), getKingPosition(teamColor));
        boolean pieceCanBlock = (threatsToKing.size() == 1)
                && threateningPieces(teamColor, threatsToKing.getFirst())
                    .stream()
                    .map(board::getPiece)
                    .anyMatch(piece -> piece.getPieceType() != ChessPiece.PieceType.KING);
        return gameOver = (isInCheck(teamColor) && !kingHasValidMoves(teamColor) && !pieceCanBlock);
    }

    /**
     * @param teamColor
     * @param pos
     * @return the pieces of a given color that threaten the position
     */
    private ArrayList<ChessPosition> threateningPieces(TeamColor teamColor, ChessPosition pos) {
        return new ArrayList<>(getTeamPositions(teamColor)
                .stream()
                .filter(position -> {
                    return validMoves(position)
                            .stream()
                            .map(ChessMove::getEndPosition)
                            .toList()
                            .contains(pos);
                })
                .toList());
    }

    /**
     * @param teamColor color
     * @return whether the given team's king has valid moves available
     */
    private boolean kingHasValidMoves(TeamColor teamColor) {
        var pos = getKingPosition(teamColor);
        validMoves(pos).forEach(System.out::println);
        return !validMoves(pos).isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        return gameOver = (!isInCheck(teamColor) && getTeamPositions(teamColor).stream().allMatch(position -> {
            return validMoves(position, board).isEmpty();
        }));
    }

    /**
     * Gets all piece positions of the given team color
     * @param teamColor color
     * @return all positions of pieces of the color
     */
    private Collection<ChessPosition> getTeamPositions(TeamColor teamColor) {
        return getTeamPositions(teamColor, board);
    }
    private Collection<ChessPosition> getTeamPositions(TeamColor teamColor, ChessBoard board) {
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
        return getKingPosition(teamColor, board);
    }
    private ChessPosition getKingPosition(TeamColor teamColor, ChessBoard board) {
        var positions = getTeamPositions(teamColor, board);
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

    public void setGameOver(boolean gameOver) {
        this.gameOver = gameOver;
    }
    public boolean isGameOver() {
        return this.gameOver;
    }

    /**
     * Gets the current chessboard
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
