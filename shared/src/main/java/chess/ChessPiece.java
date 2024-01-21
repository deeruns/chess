package chess;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final PieceType type;
    private final ChessGame.TeamColor pieceColor;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.type = type;
        this.pieceColor = pieceColor;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return type == that.type && pieceColor == that.pieceColor;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, pieceColor);
    }
    @Override
    public String toString() {
        return "ChessPiece{" +
                "type=" + type +
                ", pieceColor=" + pieceColor +
                '}';
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
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        //this wants the moves that the piece will make, but I won't implement that yet
       Collection<ChessMove> validMoves = new HashSet<>();
       // add moves to validMoves collection
        switch (this.type){
            case BISHOP ->  this.bishopMoves(validMoves, myPosition, board);
            case KING -> this.kingMoves(validMoves, myPosition, board);
            case KNIGHT -> this.knightMoves(validMoves, myPosition, board);
            case PAWN -> this.pawnMoves(validMoves, myPosition, board);
            case QUEEN ->  this.queenMoves(validMoves, myPosition, board);
            case ROOK -> this.rookMoves(validMoves, myPosition, board);
            default -> throw new UnsupportedOperationException("ERROR");
        }
        return validMoves;
    }

    /**
     * bishop moves
     * add new chess move if:
     * it isn't out of bounds
     * it isn't blocked by another piece of its own color
     * if endPosition is null or occupied by the other team
     * @param validMoves
     * @param myPosition
     * @param board
     */
    private void bishopMoves(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessBoard board) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        //moving up diagonally right
        for (int rowOne = row + 1, colOne = col + 1; rowOne <= 8 && colOne <= 8; rowOne++, colOne++) {
            ChessPosition endPosition = new ChessPosition(rowOne, colOne);
            //if there is no piece at endPosition then it is a valid move
            if (board.getPiece(endPosition) == null) {
                ChessMove newBishopMove = new ChessMove(myPosition, endPosition, null);
                validMoves.add(newBishopMove);
            }
            //taking the opposing players piece
            else if (board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                ChessMove newBishopMove = new ChessMove(myPosition, endPosition, null);
                validMoves.add(newBishopMove);
                break;
            }
            //if a piece on your team is in the way break
            else if (board.getPiece(endPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                break;
            }
        }
        //down diagonally right
        for (int rowOne = row - 1, colOne = col + 1; rowOne > 0 && colOne <= 8; rowOne--, colOne++){
            ChessPosition endPosition = new ChessPosition(rowOne, colOne);
            //if there is no piece at endPosition then it is a valid move
            if (board.getPiece(endPosition) == null) {
                ChessMove newBishopMove = new ChessMove(myPosition, endPosition, null);
                validMoves.add(newBishopMove);
            }
            //taking the opposing players piece
            else if (board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                ChessMove newBishopMove = new ChessMove(myPosition, endPosition, null);
                validMoves.add(newBishopMove);
                break;
            }
            //if a piece on your team is in the way break
            else if (board.getPiece(endPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                break;
            }
        }
        //moving down diagonally left
        for (int rowOne = row - 1, colOne = col - 1; rowOne > 0 && colOne > 0; rowOne--, colOne--){
            ChessPosition endPosition = new ChessPosition(rowOne, colOne);
            //if there is no piece at endPosition then it is a valid move
            if (board.getPiece(endPosition) == null) {
                ChessMove newBishopMove = new ChessMove(myPosition, endPosition, null);
                validMoves.add(newBishopMove);
            }
            //taking the opposing players piece
            else if (board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                ChessMove newBishopMove = new ChessMove(myPosition, endPosition, null);
                validMoves.add(newBishopMove);
                break;
            }
            //if a piece on your team is in the way break
            else if (board.getPiece(endPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                break;
            }
        }
        //moving up diagonally left
        for (int rowOne = row + 1, colOne = col - 1; rowOne <= 8 && colOne > 0; rowOne++, colOne--){
            ChessPosition endPosition = new ChessPosition(rowOne, colOne );
            //if there is no piece at endPosition then it is a valid move
            if (board.getPiece(endPosition) == null) {
                ChessMove newBishopMove = new ChessMove(myPosition, endPosition, null);
                validMoves.add(newBishopMove);
            }
            //taking the opposing players piece
            else if (board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                ChessMove newBishopMove = new ChessMove(myPosition, endPosition, null);
                validMoves.add(newBishopMove);
                break;
            }
            //if a piece on your team is in the way break
            else if (board.getPiece(endPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                break;
            }
        }

    }

    /**
     * same as diagonal but I thought of a better way to implement it.
     * calculates all moves a rook can make.
     * @param validMoves
     * @param myPosition
     * @param board
     * @param rowCount
     * @param colCount
     */
    private void straightMoves(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessBoard board, int rowCount, int colCount){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();


            for (int rowNew = row + rowCount, colNew = col + colCount; rowNew > 0 && rowNew <= 8 && colNew > 0 && colNew <= 8; rowNew += rowCount, colNew += colCount){
                ChessPosition endPosition = new ChessPosition(rowNew, colNew);

                    if (board.getPiece(endPosition)==null){
                        ChessMove newStraightMove = new ChessMove(myPosition, endPosition, null);
                        validMoves.add(newStraightMove);
                    }
                    else if (board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
                        ChessMove newStraightMove = new ChessMove(myPosition, endPosition, null);
                        validMoves.add(newStraightMove);
                        break;
                    }
                    else if (board.getPiece(endPosition).getTeamColor() == board.getPiece(myPosition).getTeamColor()){
                        break;
                    }
                }

    }
    private void kingMovesCalc(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessBoard board, int rowCount, int colCount){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(row+rowCount, col+colCount);
        if (board.getPiece(endPosition)==null){
            ChessMove newKingMove = new ChessMove(myPosition, endPosition, null);
            validMoves.add(newKingMove);
        }
//        if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN){
//            ChessPosition pawnBlock = new ChessPosition(row+1, col);
//        }
        else if (board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            ChessMove newKingMove = new ChessMove(myPosition, endPosition, null);
            validMoves.add(newKingMove);
        }
    }


    private void kingMoves(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessBoard board){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if(row > 1 && row < 8 && col > 1 && col < 8){
            kingMovesCalc(validMoves, myPosition, board, 1, 0);
            kingMovesCalc(validMoves, myPosition, board, 0, 1);
            kingMovesCalc(validMoves, myPosition, board, -1, 0);
            kingMovesCalc(validMoves, myPosition, board, 0, -1);
            kingMovesCalc(validMoves, myPosition, board, -1, -1);
            kingMovesCalc(validMoves, myPosition, board, 1, 1);
            kingMovesCalc(validMoves, myPosition, board, -1, 1);
            kingMovesCalc(validMoves, myPosition, board, 1, -1);

        }

    }
    private void knightMoves(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessBoard board){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row <= 6 && col < 8){kingMovesCalc(validMoves, myPosition, board, 2,1);}
        if (row <= 6 && col > 1){kingMovesCalc(validMoves, myPosition, board, 2,-1);}
        if (row >= 3 && col < 8){kingMovesCalc(validMoves, myPosition, board, -2,1);}
        if (row >= 3 && col > 1){kingMovesCalc(validMoves, myPosition, board, -2,-1);}
        if (col <= 6 && row < 8){kingMovesCalc(validMoves, myPosition, board, 1,2);}
        if (col <= 6 && row > 1){kingMovesCalc(validMoves, myPosition, board, -1,2);}
        if (col >= 3 && row < 8){kingMovesCalc(validMoves, myPosition, board, 1,-2);}
        if (col >= 3 && row > 1){kingMovesCalc(validMoves, myPosition, board, -1,-2);}
    }
    private void queenMoves(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessBoard board){
        straightMoves(validMoves, myPosition, board, 1,0);
        straightMoves(validMoves, myPosition, board, 0,1);
        straightMoves(validMoves, myPosition, board, -1,0);
        straightMoves(validMoves, myPosition, board, 0,-1);
        bishopMoves(validMoves, myPosition, board);
    }
    private void rookMoves(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessBoard board){
        straightMoves(validMoves, myPosition, board, 1,0);
        straightMoves(validMoves, myPosition, board, 0,1);
        straightMoves(validMoves, myPosition, board, -1,0);
        straightMoves(validMoves, myPosition, board, 0,-1);
    }
    private void pawnMoves(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessBoard board){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
//        int pawnBlockWhite = myPosition.getRow() + 1;
//        int pawnBlockBlack = myPosition.getRow() - 1;
        ChessPosition whiteBlockMove = new ChessPosition(row + 1, col);
        ChessPosition blackBlockMove = new ChessPosition(row - 1, col);
        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){
            if (row == 2){
                kingMovesCalc(validMoves, myPosition, board, 1, 0);
                //check to see if a piece is in front of the pawn at start position
                if (board.getPiece(whiteBlockMove) == null)
                    kingMovesCalc(validMoves, myPosition, board, 2, 0);
            }
        }

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            if (row == 7){
                kingMovesCalc(validMoves, myPosition, board, -1, 0);
                //check to see if a piece is in front of the pawn at start position
                if (board.getPiece(blackBlockMove) == null)
                    kingMovesCalc(validMoves, myPosition, board, -2, 0);
            }
        }
    }
}
