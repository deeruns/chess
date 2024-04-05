package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {
    public static final UserGameCommand.CommandType type = UserGameCommand.CommandType.JOIN_PLAYER;
    ChessGame.TeamColor teamColor;
    int gameID;
    public JoinPlayerCommand(String authToken, int gameID, ChessGame.TeamColor teamColor){
        super(authToken);
        this.gameID = gameID;
        this.teamColor = teamColor;
    }

}
