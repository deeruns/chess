package server.websocket;

import Models.AuthTokenData;
import com.google.gson.Gson;
import dataAccess.*;
import dataaccess.DataAccess;
import exception.ResponseException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.Action;
import webSocketMessages.Notification;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;

import java.io.IOException;
import java.util.Timer;


@WebSocket
public class WebSocketHandler {
    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;

    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler() throws DataAccessException {
        try {
            this.userDAO = new SqlUserDAO();
            this.authDAO = new SqlAuthDAO();
            this.gameDAO = new SqlGameDAO();
        } catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer((JoinPlayerCommand) command, session);
            case JOIN_OBSERVER -> joinObserver((JoinObserverCommand) command, session);
            case MAKE_MOVE -> makeMove((MakeMoveCommand) command, session);
            case LEAVE -> leaveGame((LeaveGameCommand) command, session);
            case RESIGN -> resignGame((ResignCommand) command, session);
        }
    }

    private void joinPlayer(JoinPlayerCommand message, Session session) throws IOException, DataAccessException {
        try{
            //add player to gameSessions map
            connections.add(message.getGameID(), message.getAuthString(), session);
            //broadcast that player has joined game
            AuthTokenData authData = authDAO.getUser(message.getAuthString());
            String username = authData.username();
            var messageString = String.format("%s has joined the game", username);
            //var notification = new UserGameCommand(UserGameCommand.CommandType.JOIN_PLAYER, message);
            connections.broadcast(messageString, notification);
        }
        catch(Exception exception){
            throw new IOException(exception.getMessage());
        }
    }

    private void joinObserver(JoinObserverCommand message, Session session) throws IOException {
        connections.add(message.getGameID(), message.getAuthString(), session);
        //broadcast
        var message = String.format("%s left the shop", visitorName);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(visitorName, notification);
    }
    private void makeMove(MakeMoveCommand message, Session session) throws IOException {
        var message = String.format("%s left the shop", visitorName);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(visitorName, notification);
    }
    private void leaveGame(LeaveGameCommand message, Session session) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the shop", visitorName);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(visitorName, notification);
    }
    private void resignGame(ResignCommand message, Session session) throws IOException {
        connections.remove(visitorName);
        var message = String.format("%s left the shop", visitorName);
        var notification = new Notification(Notification.Type.DEPARTURE, message);
        connections.broadcast(visitorName, notification);
    }



    public void makeNoise(String petName, String sound) throws ResponseException {
        try {
            var message = String.format("%s says %s", petName, sound);
            var notification = new Notification(Notification.Type.NOISE, message);
            connections.broadcast("", notification);
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }
}
