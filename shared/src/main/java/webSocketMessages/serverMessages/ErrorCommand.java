package webSocketMessages.serverMessages;

public class ErrorCommand extends ServerMessage {
    String errorMessage;
    public static final ServerMessage.ServerMessageType type = ServerMessageType.ERROR;

    public ErrorCommand(ServerMessageType type, String errorMessage){
        super(type);
        this.errorMessage = errorMessage;
    }
}
