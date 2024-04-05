package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    int gameID;
    ChessMove chessMove;
    public MakeMoveCommand(String authToken, int gameID, ChessMove move){
        super(authToken);
        this.chessMove = move;
        this.gameID = gameID;
    }
}
