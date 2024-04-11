package webSocketMessages.userCommands;

import chess.ChessGame;
import chess.ChessMove;

import java.util.Objects;

/**
 * Represents a command a user can send the server over a websocket
 *
 * Note: You can add to this class, but you should not alter the existing
 * methods.
 */
public class UserGameCommand {

    public UserGameCommand(String authToken, CommandType type) {
        this.authToken = authToken;
        this.commandType = type;
//        this.gameID = gameID;
//        this.playerColor = playerColor;
//        this.move = move;
    }

    public enum CommandType {
        JOIN_PLAYER,
        JOIN_OBSERVER,
        MAKE_MOVE,
        LEAVE,
        RESIGN
    }

    protected CommandType commandType;

    private final String authToken;
    //private int gameID;
//    private ChessGame.TeamColor teamColor;
//    private ChessMove move;

    public String getAuthString() {
        return authToken;
    }

    public CommandType getCommandType() {
        return this.commandType;
    }

//    public int getGameID() {
//        return gameID;
//    }
//
//    public ChessGame.TeamColor getTeamColor() {
//        return teamColor;
//    }
//
//    public ChessMove getMove() {
//        return move;
//    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserGameCommand))
            return false;
        UserGameCommand that = (UserGameCommand) o;
        return getCommandType() == that.getCommandType() && Objects.equals(getAuthString(), that.getAuthString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCommandType(), getAuthString());
    }
}
