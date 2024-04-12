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
    public Status state;
    public enum Status {
        PLAYING,
        OVER
    }
    //If i am referencing the board by indexing, I need to do -1 to get intp 0,7 format. but if get.row is called it isn't necessary?

    public ChessGame() {
        this.playerTurn = TeamColor.WHITE;
        currentBoard = new ChessBoard();
        currentBoard.resetBoard();
        state = Status.PLAYING;
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
        ChessPiece currentPiece = currentBoard.getPiece(startPosition);
        validMoves = (HashSet<ChessMove>) currentPiece.pieceMoves(currentBoard, startPosition);
        TeamColor team = currentPiece.getTeamColor();
        HashSet<ChessMove> validMovesFinal = new HashSet<>();

        Iterator<ChessMove> validMovesIter = validMoves.iterator();
        //ChessBoard cloneBoard = BoardCopy2(currentBoard);
        while(validMovesIter.hasNext()){
            //reset the board to the original
            ChessBoard cloneBoard = boardCopy2(currentBoard);
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
            //reset the board to the original -- for some reason board clone is not working
            //currentBoard = cloneBoard;
            currentBoard.addPiece(start, currentPiece);
            currentBoard.addPiece(end, null);
        }
        //currentBoard = cloneBoard;
        return validMovesFinal;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();
        ChessPiece.PieceType pawnPromo = move.getPromotionPiece();
        ChessPiece currentPiece = currentBoard.getPiece(startPosition);
        TeamColor currentPieceColor = currentBoard.getPiece(startPosition).getTeamColor();
        Collection<ChessMove> currPieceMoves = currentBoard.getPiece(startPosition).pieceMoves(currentBoard, startPosition);
        if (currentPiece.getTeamColor() == playerTurn) {
            //if move is a validMove
            if(!validMoves(startPosition).contains(move)){
                throw new InvalidMoveException();
            }
            for (ChessMove validmoves : validMoves(startPosition)) {
                if (move.equals(validmoves)) {
                    if (pawnPromo == null) {
                        currentBoard.addPiece(startPosition, null);
                        //add normal piece
                        currentBoard.addPiece(endPosition, currentPiece);
                        if (playerTurn == TeamColor.WHITE) {
                            setTeamTurn(TeamColor.BLACK);
                        } else {
                            setTeamTurn(TeamColor.WHITE);
                        }
                    } else {
                        currentBoard.addPiece(startPosition, null);
                        //add promo piece
                        currentBoard.addPiece(endPosition, new ChessPiece(currentPieceColor, pawnPromo));
                        if (playerTurn == TeamColor.WHITE) {
                            setTeamTurn(TeamColor.BLACK);
                        } else {
                            setTeamTurn(TeamColor.WHITE);
                        }
                    }
                }
            }
        }
        else{throw new InvalidMoveException();}
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //make a clone of the board
        boardCopy(currentBoard);
        //all possible moves of the other team
        Collection<ChessMove> opponentPieceMoves = opponentPieceMoves(teamColor);
        //find the position of the king and get its validMoves
        ChessPosition kingLocation = findKingPiece(teamColor);
        if (kingLocation == null){
            return false;
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

    public ChessPosition findKingPiece(TeamColor teamColor){
        //find the position of the king and get its validMoves
        for (int col = 1; col > 0 && col <= 8; col++){
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
        //compare opponent moves to kingmoves and if any match up, then remove that move
        ChessPosition kingLocation = findKingPiece(teamColor);
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
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessPosition kingLocation = findKingPiece(teamColor);
        if (!isInCheck(teamColor)){
            // if the king can't make a valid move
            if (validMoves(kingLocation).isEmpty()){
                //if the king is in start position and can't make any moves, we don't want it to also be in stalemate
                if(currentBoard.getPiece(kingLocation).pieceMoves(currentBoard, kingLocation).isEmpty()) {
                    return false;
                }
                else{return true;}
            }
        }
        return false;
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
    private void boardCopy(ChessBoard board){
        boardClone = new ChessPiece[8][8];
        for (int col = 0; col <= 7; col++){

            for (int row = 0; row <= 7; row++){

                boardClone[col][row] = board.boardDimension[col][row];
            }
        }
    }

    private ChessBoard boardCopy2(ChessBoard board){
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

