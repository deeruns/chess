package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    ChessBoard currentBoard;
    TeamColor playerTurn;
    ChessPiece[][] boardClone;

    public ChessGame() {
        currentBoard = new ChessBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return playerTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        playerTurn = team;
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
        if (currentBoard.getPiece(startPosition) == null){
            return null;
        }

        HashSet<ChessMove> validMoves;
        //ChessPiece holdingPiece = new ChessPiece(TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        ChessPiece currentPiece = currentBoard.getPiece(startPosition);

        validMoves = (HashSet<ChessMove>) currentPiece.pieceMoves(currentBoard, startPosition);

        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //make a clone of the board
        BoardCopy(currentBoard);
        //Collection<ChessMove> kingValidMoves = new ChessPiece.pieceMoves(currentBoard, kingLocation);
        //find the position of the king and get its validMoves
        ChessPosition kingLocation = FindKingPiece(teamColor);
        Collection<ChessMove> kingValidMoves = boardClone[kingLocation.getRow()][kingLocation.getColumn()].pieceMoves(currentBoard, kingLocation);



        return true;
    }

    public ChessPosition FindKingPiece(TeamColor teamColor){
        //does this check a different board than is in check?
        //find the position of the king and get its validMoves
        for (int col = 1; col > 0 && col < 8; col++){
            //ChessPosition position = new ChessPosition(row, col);
            for(int row = 1;row > 0 && row < 8;row++){
                //find the king
                if (boardClone[row][col] != null){
                    if (boardClone[row][col].getTeamColor() == teamColor && boardClone[row][col].getPieceType() == ChessPiece.PieceType.KING){
                        return new ChessPosition(row, col);
                    }

            }
            }
        }
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currentBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }
    private void BoardCopy(ChessBoard board){
        boardClone = new ChessPiece[8][8];
        for (int col = 0; col <= 7; col++){

            for (int row = 0; row <= 7; row++){

                boardClone[col][row] = board.boardDimension[col][row];
            }
        }
    }
}

