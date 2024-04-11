package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    public int gameID;
    public ChessMove move;
    public MakeMoveCommand(String authToken, int gameID, ChessMove move){
        super(authToken, CommandType.MAKE_MOVE);
        this.move = move;
        this.gameID = gameID;
    }
}
