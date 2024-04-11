package webSocketMessages.serverMessages;

import webSocketMessages.userCommands.UserGameCommand;

public class NotificationCommand extends ServerMessage {
    String message;
    public static final ServerMessage.ServerMessageType type = ServerMessage.ServerMessageType.NOTIFICATION;

    public NotificationCommand(ServerMessageType type, String message){
        super(type);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
