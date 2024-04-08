package server.websocket;

import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.serverMessages.ServerMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public static HashMap<Integer, HashMap<String, Session>> gameSessions = new HashMap<>();

    public void add(int gameID, String auth, Session session) {
        gameSessions.get(gameID).put(auth, session);
    }

    public void remove(int gameID, String auth, Session session) {
        gameSessions.get(gameID).remove(auth, session);
    }
    public void addGame(int gameID, String auth, Session session) {
        gameSessions.get(gameID).put(auth, session);
    }

    public HashMap<String, Session> getGame(int gameID){
        for (Integer game : gameSessions.keySet()){
            if (game == gameID) {
                return gameSessions.get(game);
            }
        }
        return null;
    }
    public int getGameID(String auth) {
        for (Integer gameID : gameSessions.keySet()){
            for (String authToken : gameSessions.get(gameID).keySet()){
                if (authToken.equals(auth)){
                    return gameID;
                }
            }
        }
        return 0;
    }
    public void removeSession(Session session){
        for (Integer ID : gameSessions.keySet()){
            for (String authToken : gameSessions.get(ID).keySet()){
                if (gameSessions.get(ID).get(authToken).equals(session)){
                    gameSessions.get(ID).remove(authToken, session);
                }
            }
        }
    }



    public void broadcast(String excludePlayerName, UserGameCommand gameCommand) throws IOException {
        var removeList = new ArrayList<Connection>();
        for (var c : connections.values()) {
            if (c.session.isOpen()) {
                if (!c.playerName.equals(excludePlayerName)) {
                    c.send(gameCommand.toString());
                }
            } else {
                removeList.add(c);
            }
        }

        // Clean up any connections that were left open.
        for (var c : removeList) {
            connections.remove(c.playerName);
        }
    }
}
