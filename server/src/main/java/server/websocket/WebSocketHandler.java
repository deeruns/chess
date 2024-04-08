package server.websocket;

import Models.AuthTokenData;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.LoadGameCommand;
import webSocketMessages.serverMessages.NotificationCommand;
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
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
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
            //LoadGameCommand loadGameCommand = new LoadGameCommand(ServerMessage.ServerMessageType.LOAD_GAME, gameDAO.getGame(message.getGameID()).game())
            var messageString = String.format("%s has joined the game as %s", username, message.getTeamColor());
            var notification = new NotificationCommand(ServerMessage.ServerMessageType.NOTIFICATION, messageString);
            connections.broadcast(message.getGameID(), notification, message.getAuthString());
        }
        catch(Exception exception){
            throw new IOException(exception.getMessage());
        }
    }

    private void joinObserver(JoinObserverCommand message, Session session) throws IOException {
        try{
            //add player to gameSessions map
            connections.add(message.getGameID(), message.getAuthString(), session);
            //broadcast that player has joined game
            AuthTokenData authData = authDAO.getUser(message.getAuthString());
            String username = authData.username();
            var messageString = String.format("%s has joined the game as an observer", username);
            var notification = new NotificationCommand(ServerMessage.ServerMessageType.NOTIFICATION, messageString);
            connections.broadcast(message.getGameID(), notification, message.getAuthString());
        }
        catch(Exception exception){
            throw new IOException(exception.getMessage());
        }
    }
    private void makeMove(MakeMoveCommand message, Session session) throws IOException {
        try{
            //broadcast that player made a move
            ChessMove move = message.getMove();
            ChessPosition startPos = move.getStartPosition();
            ChessPosition endPos = move.getEndPosition();
            AuthTokenData authData = authDAO.getUser(message.getAuthString());
            String username = authData.username();
            var messageString = String.format("%s has moved a piece from %s to %s", username, startPos, endPos);
            var notification = new NotificationCommand(ServerMessage.ServerMessageType.NOTIFICATION, messageString);
            connections.broadcast(message.getGameID(), notification, message.getAuthString());
        }
        catch(Exception exception){
            throw new IOException(exception.getMessage());
        }
    }
    private void leaveGame(LeaveGameCommand message, Session session) throws IOException {
        try{
            AuthTokenData authData = authDAO.getUser(message.getAuthString());
            String username = authData.username();
            connections.remove(message.getGameID(), message.getAuthString(), session);
            var messageString = String.format("%s has left the game", username);
            var notification = new NotificationCommand(ServerMessage.ServerMessageType.NOTIFICATION, messageString);
            connections.broadcast(message.getGameID(), notification, message.getAuthString());
        }
        catch(Exception exception){
            throw new IOException(exception.getMessage());
        }
    }
    private void resignGame(ResignCommand message, Session session) throws IOException {
        try{
            AuthTokenData authData = authDAO.getUser(message.getAuthString());
            String username = authData.username();
            connections.remove(message.getGameID(), message.getAuthString(), session);
            var messageString = String.format("%s has left the game", username);
            var notification = new NotificationCommand(ServerMessage.ServerMessageType.NOTIFICATION, messageString);
            connections.broadcast(message.getGameID(), notification, message.getAuthString());
        }
        catch(Exception exception){
            throw new IOException(exception.getMessage());
        }
    }

}
