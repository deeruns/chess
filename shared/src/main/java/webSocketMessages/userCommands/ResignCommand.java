package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

public class ResignCommand extends UserGameCommand{
    public int gameID;
    public static final UserGameCommand.CommandType type = CommandType.RESIGN;
    public ResignCommand(String authToken, int gameID){
        super(authToken, CommandType.RESIGN);
        this.gameID = gameID;
    }
}
