package WebSocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.UserGameCommand;
import webSocketMessages.userCommands.UserGameCommand.CommandType;

//import javax.management.Notification;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;


    public WebSocketFacade(String url, NotificationHandler notificationHandler) throws DataAccessException {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/connect");
            this.notificationHandler = notificationHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void resign(String authToken, int gameID) throws DataAccessException {
        commandHelp(authToken, gameID, CommandType.RESIGN, null, null);
    }
    public void leave(String authToken, int gameID) throws DataAccessException {
        commandHelp(authToken, gameID, CommandType.LEAVE, null, null);
    }
    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) throws DataAccessException {
        commandHelp(authToken, gameID, CommandType.JOIN_PLAYER, playerColor, null);
    }

    public void joinObserver(String authToken, int gameID) throws DataAccessException {
        commandHelp(authToken, gameID, CommandType.JOIN_OBSERVER, null, null);
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws DataAccessException {
        commandHelp(authToken, gameID, CommandType.MAKE_MOVE, null, move);
    }


    private void commandHelp(String authToken, int gameID, UserGameCommand.CommandType type, ChessGame.TeamColor playerColor, ChessMove move ) throws DataAccessException {
        try {
            var action = new UserGameCommand(authToken, gameID, type, playerColor, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
