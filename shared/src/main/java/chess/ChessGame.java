package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

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
    //If i am referencing the board by indexing, I need to do -1 to get intp 0,7 format. but if get.row is called it isn't necessary?

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
        TeamColor team = currentPiece.getTeamColor();
        HashSet<ChessMove> validMovesFinal = new HashSet<>();

        Iterator<ChessMove> validMovesIter = validMoves.iterator();
        while(validMovesIter.hasNext()){
            //reset the board to the original
            ChessBoard cloneBoard = BoardCopy2(currentBoard);
            ChessMove move = validMovesIter.next();
            ChessPosition end = move.getEndPosition();
            ChessPosition start = move.getStartPosition();

            //move the actual piece on the board
            currentBoard.addPiece(end, currentPiece);
            currentBoard.addPiece(start, null);
            //if the piece does not leave you in check, then the move is valid
            if(!(isInCheck(currentPiece.getTeamColor()))){
                validMovesFinal.add(move);
            }
            //reset the board to the original
            currentBoard = cloneBoard;
        }
        //this.currentBoard = cloneBoard;
        return validMovesFinal;
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
        //all possible moves of the other team
        Collection<ChessMove> opponentPieceMoves = opponentPieceMoves(teamColor);
        //find the position of the king and get its validMoves
        ChessPosition kingLocation = FindKingPiece(teamColor);
        if (kingLocation == null){
            return true;
        }
        Collection<ChessMove> kingValidMoves = currentBoard.getPiece(new ChessPosition(kingLocation.getRow(), kingLocation.getColumn())).pieceMoves(currentBoard, kingLocation);
        //check if the opponent moves can put my king in check
        for (ChessMove move : opponentPieceMoves) {
            if (move.getEndPosition().equals(kingLocation)){
                return true;
            }
        }
        return false;
    }

    public ChessPosition FindKingPiece(TeamColor teamColor){
        //does this check a different board than is in check?
        //find the position of the king and get its validMoves
        for (int col = 1; col > 0 && col <= 8; col++){
            //ChessPosition position = new ChessPosition(row, col);
            for(int row = 1; row > 0 && row <= 8;row++){
                //find the king
                if (currentBoard.getPiece(new ChessPosition(row, col)) != null) {
                    if (currentBoard.getPiece(new ChessPosition(row, col)).getTeamColor() == teamColor && currentBoard.getPiece(new ChessPosition(row, col)).getPieceType() == ChessPiece.PieceType.KING) {
                        return new ChessPosition(row, col);
                    }
                }
            }
        }
        return null;
    }
    public Collection<ChessMove> opponentPieceMoves(TeamColor teamColor){
        //does this check a different board than is in check?
        //find the position of the king and get its validMoves
        Collection<ChessMove> opponentPieceMoves = new HashSet<>();
        for (int col = 1; col > 0 && col <= 8; col++){
            //ChessPosition position = new ChessPosition(row, col);
            for(int row = 1; row > 0 && row <= 8; row++){
                //find the king
                if (currentBoard.getPiece(new ChessPosition(row, col)) != null) {
                    if (currentBoard.getPiece(new ChessPosition(row, col)).getTeamColor() != teamColor) {
                        opponentPieceMoves.addAll(currentBoard.getPiece(new ChessPosition(row, col)).pieceMoves(currentBoard, new ChessPosition(row, col)));
                    }
                }
            }
        }
        return opponentPieceMoves;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //        BoardCopy(currentBoard);
//        //compare opponent moves to kingmoves and if any match up, then remove that move
        ChessPosition kingLocation = FindKingPiece(teamColor);
        //Collection<ChessMove> kingValidMoves = currentBoard.getPiece(new ChessPosition(kingLocation.getRow(), kingLocation.getColumn())).pieceMoves(currentBoard, kingLocation);
        //Collection<ChessMove> opponentPieceMoves = opponentPieceMoves(teamColor);
        if (!isInCheck(teamColor)){
            //can't be in checkmate if it isn't in check
            return false;
        }
        if(isInCheck(teamColor)) {
            //if the king can't make any valid moves it is in checkmate
            if (validMoves(kingLocation).isEmpty()) {
                return true;
            }
        }
        return false;
//        Iterator<ChessMove> kingMovesIter = kingValidMoves.iterator();
//        while(kingMovesIter.hasNext()){
//            ChessMove kingMove = kingMovesIter.next();
//            ChessPosition kingMoveEnd = kingMove.endPosition;
//
//            for (ChessMove oppMove: opponentPieceMoves){
//                //for(ChessMove kingMove: kingValidMoves){
//                ChessPosition oppMoveEnd = oppMove.endPosition;
//                //ChessPosition kingMoveEnd = kingMove.endPosition;
//
//                if(oppMoveEnd == kingMoveEnd){
//                    kingMovesIter.remove();
//                    break;
//                }
//            }
//        }
//        if (kingValidMoves.isEmpty()){
//            return true;
//        }
//        return false;
//        return kingValidMoves.isEmpty();
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

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

    private ChessBoard BoardCopy2(ChessBoard board){
        ChessBoard cloneBoard = new ChessBoard();
        for (int col = 1; col <= 8; col++){
            for (int row = 1; row <= 8; row++){
                ChessPosition newspot = new ChessPosition(row, col);
                if (board.getPiece(newspot) != null)
                    cloneBoard.addPiece(newspot, board.getPiece(newspot));
            }
        }
        return cloneBoard;
    }

}

