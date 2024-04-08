package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.userCommands.UserGameCommand;

import java.io.IOException;

public class Connection {
    public String message;
    public String gameID;
    public Session session;

    public Connection(String message, String gameID, Session session) {
        this.message = message;
        this.gameID = gameID;
        this.session = session;
    }
    public static HashMap<Integer, HashMap<String, Session>> sessionMap = new HashMap<>();

    public void send(String msg) throws IOException {
        session.getRemote().sendString(msg);
    }
}
