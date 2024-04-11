package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand {
    public static final UserGameCommand.CommandType type = UserGameCommand.CommandType.JOIN_PLAYER;
    public ChessGame.TeamColor playerColor;
    public int gameID;
    public JoinPlayerCommand(String authToken, int gameID, ChessGame.TeamColor teamColor){
        super(authToken, CommandType.JOIN_PLAYER);
        this.gameID = gameID;
        this.playerColor = teamColor;
    }

//        public int getGameID() {
//        return gameID;
//    }
//
//    public ChessGame.TeamColor getTeamColor() {
//        return teamColor;
//    }


}
