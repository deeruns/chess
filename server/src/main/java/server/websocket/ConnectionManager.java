package server.websocket;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import webSocketMessages.serverMessages.ErrorCommand;
import webSocketMessages.serverMessages.NotificationCommand;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.serverMessages.ServerMessage;

import javax.management.Notification;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public static HashMap<Integer, HashMap<String, Session>> gameSessions = new HashMap<>();

    public void add(int gameID, String auth, Session session) {
        //add game if not in the hashmap
        gameSessions.computeIfAbsent(gameID, k -> new HashMap<String, Session>());
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

    private void sendMessage(int gameID, ServerMessage serverMessage, String authToken) throws IOException {
        HashMap<String, Session> game = getGame(gameID);
        Session session = game.get(authToken);
        session.getRemote().sendString(new Gson().toJson(serverMessage));
    }

    public void broadcast(int gameID, ServerMessage message, String exceptThisAuthToken) throws IOException {
        HashMap<String, Session> game = getGame(gameID);
        var removeList = new ArrayList<Session>();
        for (Map.Entry<String, Session> entry : game.entrySet()) {
            String authToken = entry.getKey();
            Session session = entry.getValue();
//            if (!authToken.equals(exceptThisAuthToken)) {
//                    session.getRemote().sendString(new Gson().toJson(message));
//                }
            if (session.isOpen()) {
                if (!authToken.equals(exceptThisAuthToken)) {
                    session.getRemote().sendString(new Gson().toJson(message));
                }
            }
            else{
                removeList.add(session);
            }
        }
        for(var c : removeList){
            removeSession(c);
        }
    }

    public void broadcastLoadToAll(int gameID, ServerMessage message, String exceptThisAuthToken) throws IOException {
        HashMap<String, Session> game = getGame(gameID);
        var removeList = new ArrayList<Session>();
        for (Map.Entry<String, Session> entry : game.entrySet()) {
            String authToken = entry.getKey();
            Session session = entry.getValue();
            //session.getRemote().sendString(new Gson().toJson(message));
            if(session.isOpen()){
                session.getRemote().sendString(new Gson().toJson(message));
            }
            else {
                removeList.add(session);
            }
        }
        for(var c : removeList){
            removeSession(c);
        }
    }

    public void broadcastToMe(int gameID, ServerMessage message, String exceptThisAuthToken) throws IOException {
        HashMap<String, Session> game = getGame(gameID);
        var removeList = new ArrayList<Session>();
        for (Map.Entry<String, Session> entry : game.entrySet()) {
            String authToken = entry.getKey();
            Session session = entry.getValue();
            if (session.isOpen()) {
                if (authToken.equals(exceptThisAuthToken)) {
                    session.getRemote().sendString(new Gson().toJson(message));
                }
            }
            else{
                removeList.add(session);
            }
        }
        for(var c : removeList){
            removeSession(c);
        }
    }
}


