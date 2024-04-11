package webSocketMessages.serverMessages;

import chess.ChessGame;
import webSocketMessages.userCommands.UserGameCommand;

public class LoadGameCommand extends ServerMessage{
    ChessGame game;
    public static final ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.LOAD_GAME;

    public LoadGameCommand(ServerMessageType type, ChessGame game){
        super(type);
        this.game = game;
    }
}
