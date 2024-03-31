import webSocketMessages.serverMessages.ServerMessage;

public interface ServerMessageObserver {
    public default void notify(ServerMessage message){}
}
