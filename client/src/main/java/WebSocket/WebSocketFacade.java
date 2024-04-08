package WebSocket;

import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import webSocketMessages.serverMessages.ServerMessage;
import webSocketMessages.userCommands.*;
import webSocketMessages.userCommands.UserGameCommand.CommandType;

//import javax.management.Notification;
import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

//import static jdk.internal.net.http.HttpConnection.getConnection;

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

            //message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    notificationHandler.notify(notification);
//                    try {
//                        Gson gson = new Gson();
//                        ServerMessage message =
//                                gson.fromJson(message, ServerMessage.class);
//                        observer.notify(message);
//                    } catch(Exception ex) {
//                        observer.notify(new ErrorMessage(ex.getMessage()));
//                    }

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
        try {
            var action = new ResignCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void leave(String authToken, int gameID) throws DataAccessException {
        try {
            var action = new LeaveGameCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void joinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor) throws DataAccessException {
        try {
            var action = new JoinPlayerCommand(authToken, gameID, playerColor);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void joinObserver(String authToken, int gameID) throws DataAccessException {
        try {
            var action = new JoinObserverCommand(authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws DataAccessException {
        try {
            var action = new MakeMoveCommand(authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    private void sendMessage(String authToken, int gameID, UserGameCommand.CommandType type, ChessGame.TeamColor playerColor, ChessMove move) throws DataAccessException {
        try {
            var action = new UserGameCommand(authToken);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }
//    @OnWebSocketMessage
//    public void onMessage(Session session, String msg) throws Exception {
//        GameCommand command = readJson(msg, UserGameCommand.class);
//
//        var conn = getConnection(command.authToken, session);
//        if (conn != null) {
//            switch (command.commandType) {
//                case JOIN_PLAYER -> join(conn, msg);
//                case JOIN_OBSERVER -> observe(conn, msg);
//                case MAKE_MOVE -> move(conn, msg));
//                case LEAVE -> leave(conn, msg);
//                case RESIGN -> resign(conn, msg);
//            }
//        } else {
//            Connection.sendError(session.getRemote(), "unknown user");
//        }
//    }

};
