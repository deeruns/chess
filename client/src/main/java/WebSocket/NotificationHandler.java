package WebSocket;

import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;

public interface NotificationHandler {
    void notify(String notification, ServerMessage.ServerMessageType type);
}