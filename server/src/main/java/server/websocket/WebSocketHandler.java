package server.websocket;

import Models.AuthTokenData;
import Models.GameData;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;
import com.google.gson.Gson;
import dataAccess.*;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import webSocketMessages.serverMessages.ErrorCommand;
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
            case JOIN_PLAYER -> joinPlayer(message, session);
            case JOIN_OBSERVER -> joinObserver(message, session);
            case MAKE_MOVE -> makeMove(message, session);
            case LEAVE -> leaveGame(message, session);
            case RESIGN -> resignGame(message, session);
        }
    }

    private void joinPlayer(String message, Session session) throws IOException, DataAccessException {
        try{
            JoinPlayerCommand command = new Gson().fromJson(message, JoinPlayerCommand.class);
            //add player to gameSessions map
            connections.add(command.gameID, command.getAuthString(), session);
            //broadcast that player has joined game
            AuthTokenData authData = authDAO.getUser(command.getAuthString());
            String username = authData.username();
            GameData gameData = gameDAO.getGame(command.gameID);
            LoadGameCommand loadGameCommand = new LoadGameCommand(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            session.getRemote().sendString(new Gson().toJson(loadGameCommand));
            var messageString = String.format("%s has joined the game as %s", username, command.playerColor);
            var notification = new NotificationCommand(ServerMessage.ServerMessageType.NOTIFICATION, messageString);
            connections.broadcast(command.gameID, notification, command.getAuthString());
        }
        catch(Exception exception){
            sendError(exception, session);
        }
    }

    private void joinObserver(String message, Session session) throws IOException {
        try{
            JoinObserverCommand command = new Gson().fromJson(message, JoinObserverCommand.class);
            //add player to gameSessions map
            connections.add(command.gameID, command.getAuthString(), session);
            //broadcast that player has joined game
            AuthTokenData authData = authDAO.getUser(command.getAuthString());
            String username = authData.username();
            GameData gameData = gameDAO.getGame(command.gameID);
            LoadGameCommand loadGameCommand = new LoadGameCommand(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            session.getRemote().sendString(new Gson().toJson(loadGameCommand));
            //connections.broadcast(command.gameID, loadGameCommand, command.getAuthString());
            var messageString = String.format("%s has joined the game as an observer", username);
            var notification = new NotificationCommand(ServerMessage.ServerMessageType.NOTIFICATION, messageString);
            connections.broadcast(command.gameID, notification, command.getAuthString());
        }
        catch(Exception exception){
            sendError(exception, session);
        }
    }
    private void makeMove(String message, Session session) throws IOException {
        try{
            MakeMoveCommand command = new Gson().fromJson(message, MakeMoveCommand.class);
            //broadcast that player made a move
            ChessMove move = command.move;
            ChessPosition startPos = move.getStartPosition();
            ChessPosition endPos = move.getEndPosition();
            AuthTokenData authData = authDAO.getUser(command.getAuthString());
            String username = authData.username();
            GameData gameData = gameDAO.getGame(command.gameID);
            LoadGameCommand loadGameCommand = new LoadGameCommand(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            //session.getRemote().sendString(new Gson().toJson(loadGameCommand));
            connections.broadcastLoadToAll(command.gameID, loadGameCommand, command.getAuthString());
            var messageString = String.format("%s has moved a piece from %s to %s", username, startPos, endPos);
            var notification = new NotificationCommand(ServerMessage.ServerMessageType.NOTIFICATION, messageString);
            connections.broadcast(command.gameID, notification, command.getAuthString());
            //LOAD command after Notification?
            //connections.broadcastLoadToAll(command.gameID, loadGameCommand, command.getAuthString());

            //ADD stuff for finishing the game?
        }
        catch(Exception exception){
            sendError(exception, session);
        }
    }
    private void leaveGame(String message, Session session) throws IOException {
        try{
            LeaveGameCommand command = new Gson().fromJson(message, LeaveGameCommand.class);
            AuthTokenData authData = authDAO.getUser(command.getAuthString());
            String username = authData.username();
            connections.remove(command.gameID, command.getAuthString(), session);
            var messageString = String.format("%s has left the game", username);
            var notification = new NotificationCommand(ServerMessage.ServerMessageType.NOTIFICATION, messageString);
            connections.broadcast(command.gameID, notification, command.getAuthString());
        }
        catch(Exception exception){
            sendError(exception, session);
        }
    }
    private void resignGame(String message, Session session) throws IOException {
        try{
            ResignCommand command = new Gson().fromJson(message, ResignCommand.class);
            AuthTokenData authData = authDAO.getUser(command.getAuthString());
            String username = authData.username();
            connections.remove(command.gameID, command.getAuthString(), session);
            var messageString = String.format("%s has left the game", username);
            var notification = new NotificationCommand(ServerMessage.ServerMessageType.NOTIFICATION, messageString);
            connections.broadcast(command.gameID, notification, command.getAuthString());
        }
        catch(Exception exception){
            sendError(exception, session);
        }
    }

    private void sendError(Exception e, Session session) throws IOException {
        ErrorCommand errorMessage = new ErrorCommand(ServerMessage.ServerMessageType.ERROR, String.format("Error : " + e.getMessage()));
        if (session.isOpen()){
            session.getRemote().sendString(new Gson().toJson(errorMessage));
        } else {
            connections.removeSession(session);
        }
    }


    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
    }

    @OnWebSocketError
    public void onError(Session session, Throwable throwable){
        throwable.printStackTrace();
    }

}
