package webSocketMessages.serverMessages;

import webSocketMessages.userCommands.UserGameCommand;

public class LoadGameCommand extends ServerMessage{
    String message;
    public static final ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.LOAD_GAME;

    public LoadGameCommand(ServerMessageType type, String message){
        super(type);
        this.message = message;
    }
}
