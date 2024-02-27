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
        straightMoves(validMoves, myPosition, board, 1, 1);
        straightMoves(validMoves, myPosition, board, 1, -1);
        straightMoves(validMoves, myPosition, board, -1, 1);
        straightMoves(validMoves, myPosition, board, -1, -1);
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

    /**
     * this is for pieces with moves that don't loop. ie single moves for the knight and king
     * doesn't work for pawn
     * can move if endPosition is null or a piece of another color
     * @param validMoves
     * @param myPosition
     * @param board
     * @param rowCount
     * @param colCount
     */
    private void singleMovesCalc(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessBoard board, int rowCount, int colCount){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(row+rowCount, col+colCount);

        if (board.getPiece(endPosition)==null){
            ChessMove newSingleMove = new ChessMove(myPosition, endPosition, null);
            validMoves.add(newSingleMove);
        }

        else if (board.getPiece(endPosition) != null){
                if (board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()) {
                    ChessMove newSingleMove = new ChessMove(myPosition, endPosition, null);
                    validMoves.add(newSingleMove);
                }
        }
    }

    /**
     * calculates non-capture moves for pawns
     * can move only if endPosition is null
     * if endposition is in row 8 or 1 then pawn is promoted
     * @param validMoves
     * @param myPosition
     * @param board
     * @param rowCount
     * @param colCount
     */
    private void pawnMovesCalculator(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessBoard board, int rowCount, int colCount){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(row+rowCount, col+colCount);
        if (board.getPiece(myPosition).getPieceType() == PieceType.PAWN) {
            if (board.getPiece(endPosition) == null) {
                if (row + rowCount ==8 ){
                    validMoves.add(new ChessMove(myPosition, endPosition, PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, endPosition, PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, endPosition, PieceType.BISHOP));
                    validMoves.add(new ChessMove(myPosition, endPosition, PieceType.ROOK));
                }
                else if (row + rowCount == 1){
                    validMoves.add(new ChessMove(myPosition, endPosition, PieceType.KNIGHT));
                    validMoves.add(new ChessMove(myPosition, endPosition, PieceType.QUEEN));
                    validMoves.add(new ChessMove(myPosition, endPosition, PieceType.BISHOP));
                    validMoves.add(new ChessMove(myPosition, endPosition, PieceType.ROOK));
                }
                else {
                    ChessMove newSingleMove = new ChessMove(myPosition, endPosition, null);
                    validMoves.add(newSingleMove);
                }
            }
        }
    }

    /**
     * pawn capture moves
     * promotion also included
     * @param validMoves
     * @param myPosition
     * @param board
     * @param rowCount
     * @param colCount
     */
    private void pawnDiagCalc(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessBoard board, int rowCount, int colCount){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPosition endPosition = new ChessPosition(row+rowCount, col+colCount);
        if (board.getPiece(endPosition).getTeamColor() != board.getPiece(myPosition).getTeamColor()){
            if (row + rowCount ==8 ){
                validMoves.add(new ChessMove(myPosition, endPosition, PieceType.KNIGHT));
                validMoves.add(new ChessMove(myPosition, endPosition, PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, endPosition, PieceType.BISHOP));
                validMoves.add(new ChessMove(myPosition, endPosition, PieceType.ROOK));
            }
            else if (row + rowCount == 1){
                validMoves.add(new ChessMove(myPosition, endPosition, PieceType.KNIGHT));
                validMoves.add(new ChessMove(myPosition, endPosition, PieceType.QUEEN));
                validMoves.add(new ChessMove(myPosition, endPosition, PieceType.BISHOP));
                validMoves.add(new ChessMove(myPosition, endPosition, PieceType.ROOK));
            }
            else {
                ChessMove newPawnMove = new ChessMove(myPosition, endPosition, null);
                validMoves.add(newPawnMove);
            }
        }
    }



    private void kingMoves(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessBoard board){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

            if(row < 8){singleMovesCalc(validMoves, myPosition, board, 1, 0);}
            if(col < 8){singleMovesCalc(validMoves, myPosition, board, 0, 1);}
            if(row > 1){singleMovesCalc(validMoves, myPosition, board, -1, 0);}
            if(col > 1){singleMovesCalc(validMoves, myPosition, board, 0, -1);}
            if(col>1 && row>1){singleMovesCalc(validMoves, myPosition, board, -1, -1);}
            if(col<8 && row<8){singleMovesCalc(validMoves, myPosition, board, 1, 1);}
            if(row>1 && col < 8){singleMovesCalc(validMoves, myPosition, board, -1, 1);}
            if(col>1 && row < 8){singleMovesCalc(validMoves, myPosition, board, 1, -1);}



    }
    private void knightMoves(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessBoard board){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        if (row <= 6 && col < 8){singleMovesCalc(validMoves, myPosition, board, 2,1);}
        if (row <= 6 && col > 1){singleMovesCalc(validMoves, myPosition, board, 2,-1);}
        if (row >= 3 && col < 8){singleMovesCalc(validMoves, myPosition, board, -2,1);}
        if (row >= 3 && col > 1){singleMovesCalc(validMoves, myPosition, board, -2,-1);}
        if (col <= 6 && row < 8){singleMovesCalc(validMoves, myPosition, board, 1,2);}
        if (col <= 6 && row > 1){singleMovesCalc(validMoves, myPosition, board, -1,2);}
        if (col >= 3 && row < 8){singleMovesCalc(validMoves, myPosition, board, 1,-2);}
        if (col >= 3 && row > 1){singleMovesCalc(validMoves, myPosition, board, -1,-2);}
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

    /**
     * different if statements for black and white pawns
     * if in starting position it can move up twice, but can't capture a piece moving straight
     * can only move diagonally if it can capture a piece
     * @param validMoves
     * @param myPosition
     * @param board
     */
    private void pawnMoves(Collection<ChessMove> validMoves, ChessPosition myPosition, ChessBoard board){
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        ChessPosition whiteBlockMove = new ChessPosition(row + 1, col);
        ChessPosition blackBlockMove = new ChessPosition(row - 1, col);


        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.WHITE){

            pawnMovesCalculator(validMoves, myPosition, board, 1, 0);
            if (row == 2){
                //check to see if a piece is in front of the pawn at start position
                if (board.getPiece(whiteBlockMove) == null)
                    pawnMovesCalculator(validMoves, myPosition, board, 2, 0);
            }
            //if a pawn can take a piece from the opposite team it moves diagonally
            if(row > 1 && row < 8 && col > 1 && col < 8) {
                ChessPosition whiteTake1 = new ChessPosition(row + 1, col + 1);
                ChessPosition whiteTake2 = new ChessPosition(row + 1, col - 1);
                if (board.getPiece(whiteTake1) != null && board.getPiece(whiteTake1).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    pawnDiagCalc(validMoves, myPosition, board, 1, 1);
                }
                if (board.getPiece(whiteTake2) != null && board.getPiece(whiteTake2).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    pawnDiagCalc(validMoves, myPosition, board, 1, -1);
                }
            }
        }

        if (board.getPiece(myPosition).getTeamColor() == ChessGame.TeamColor.BLACK){
            //single move forward
            pawnMovesCalculator(validMoves, myPosition, board, -1, 0);
            if (row == 7){
                //check to see if a piece is in front of the pawn at start position (double move)
                if (board.getPiece(blackBlockMove) == null)
                    pawnMovesCalculator(validMoves, myPosition, board, -2, 0);
            }
            //if a pawn can take a piece from the opposite team it moves diagonally
            if(row > 1 && row < 8 && col > 1 && col < 8) {
                ChessPosition blackTake1 = new ChessPosition(row - 1, col + 1);
                ChessPosition blackTake2 = new ChessPosition(row - 1, col - 1);
                if (board.getPiece(blackTake1) != null && board.getPiece(blackTake1).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    pawnDiagCalc(validMoves, myPosition, board, -1, 1);
                }
                if (board.getPiece(blackTake2) != null && board.getPiece(blackTake2).getTeamColor() == ChessGame.TeamColor.WHITE) {
                    pawnDiagCalc(validMoves, myPosition, board, -1, -1);
                }
            }
        }
    }
}
