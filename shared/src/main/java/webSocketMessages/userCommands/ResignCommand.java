package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

public class ResignCommand extends UserGameCommand{
    int gameID;
    public static final UserGameCommand.CommandType type = CommandType.RESIGN;
    public ResignCommand(String authToken, int gameID){
        super(authToken);
        this.gameID = gameID;
    }
}
