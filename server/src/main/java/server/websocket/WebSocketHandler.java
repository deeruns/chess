package server.websocket;

import DataAccess.DataAccessException;
import Models.AuthTokenData;
import Models.GameData;
import chess.*;
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
import java.util.Collection;
import java.util.Objects;

import static chess.ChessGame.Status.OVER;
import static chess.ChessGame.TeamColor.BLACK;
import static chess.ChessGame.TeamColor.WHITE;


@WebSocket
public class WebSocketHandler {
    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;
    ChessGame.TeamColor teamColor;
    ChessGame game;
    private final ConnectionManager connections = new ConnectionManager();

    public WebSocketHandler() throws DataAccessException {
        this.userDAO = new SqlUserDAO();
        this.authDAO = new SqlAuthDAO();
        this.gameDAO = new SqlGameDAO();
    }
    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException, DataAccessException {
        UserGameCommand command = new Gson().fromJson(message, UserGameCommand.class);
        String authToken = command.getAuthString();
        switch (command.getCommandType()) {
            case JOIN_PLAYER -> joinPlayer(message, session);
            case JOIN_OBSERVER -> joinObserver(message, session);
            case MAKE_MOVE -> makeMove(message, session);
            case LEAVE -> leaveGame(message, session);
            case RESIGN -> resignGame(message, session, authToken);
        }
    }

    private void joinPlayer(String message, Session session) throws IOException, DataAccessException {
        try{
            JoinPlayerCommand command = new Gson().fromJson(message, JoinPlayerCommand.class);
            //add player to gameSessions map
            teamColor = command.playerColor;
            connections.add(command.gameID, command.getAuthString(), session);
            //broadcast that player has joined game
            AuthTokenData authData = authDAO.getUser(command.getAuthString());
            String username = authData.username();
            GameData gameData = gameDAO.getGame(command.gameID);
            game = gameData.game();
            isGameFull(gameData, username);
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

    private void isGameFull(GameData gameData, String username) throws DataAccessException {
        //if player name is in game and team matches then don't error
//        if((Objects.equals(gameData.whiteUsername(), username)) && (teamColor == WHITE)){
//            return true;
//        }
//        if((Objects.equals(gameData.blackUsername(), username)) && (teamColor == BLACK)){
//            return true;
//        }
        if ((!Objects.equals(gameData.blackUsername(), username)) && (teamColor == BLACK)){
            throw new DataAccessException("Player Color Already Taken");
        }
        if ((!Objects.equals(gameData.whiteUsername(), username)) && (teamColor == WHITE)){
            throw new DataAccessException("Player Color Already Taken");
        }
    }

    private void joinRealGame(int gameID) throws DataAccessException {
        if (connections.getGame(gameID) == null){
            throw new DataAccessException("Game Doesn't Exist");
        }
    }

    private void joinObserver(String message, Session session) throws IOException {
        try{
            JoinObserverCommand command = new Gson().fromJson(message, JoinObserverCommand.class);
            //add player to gameSessions map
            //joinRealGame(command.gameID);
            connections.add(command.gameID, command.getAuthString(), session);
            //broadcast that player has joined game
            AuthTokenData authData = authDAO.getUser(command.getAuthString());
            String username = authData.username();
            GameData gameData = gameDAO.getGame(command.gameID);
            game = gameData.game();
            LoadGameCommand loadGameCommand = new LoadGameCommand(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            session.getRemote().sendString(new Gson().toJson(loadGameCommand));
            //connections.broadcastToMe(command.gameID, loadGameCommand, command.getAuthString());
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
            resignError(command.gameID);
            isIncheckMate();
            moveCorrectColor(startPos);
            //MAKE MOVE
            makeMoveHelper(teamColor, game, startPos, endPos, session);
            LoadGameCommand loadGameCommand = new LoadGameCommand(ServerMessage.ServerMessageType.LOAD_GAME, gameData.game());
            //session.getRemote().sendString(new Gson().toJson(loadGameCommand));
            //connections.broadcast(command.gameID, loadGameCommand, command.getAuthString());
            //BROADCAST LOAD MESSAGE TO ALL PLAYERS INCLUDING SELF
            connections.broadcastLoadToAll(command.gameID, loadGameCommand, command.getAuthString());
            var messageString = String.format("%s has moved a piece from %s to %s", username, startPos, endPos);
            var notification = new NotificationCommand(ServerMessage.ServerMessageType.NOTIFICATION, messageString);
            //BROADCAST NOTIFICATION TO ALL PLAYERS EXCEPT SELF
            connections.broadcast(command.gameID, notification, command.getAuthString());
            //connections.broadcastLoadToAll(command.gameID, notification, command.getAuthString());

            //LOAD command after Notification?
            //connections.broadcastLoadToAll(command.gameID, loadGameCommand, command.getAuthString());

            //ADD stuff for finishing the game?
        }
        catch(Exception exception){
            sendError(exception, session);
        }
    }

    private void moveCorrectColor(ChessPosition startPos) throws DataAccessException {
        ChessPiece piece = game.getBoard().getPiece(startPos);
        if (piece.getTeamColor() != teamColor){
            throw new DataAccessException("Opponents Piece Was Selected");
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
    private void resignGame(String message, Session session, String authToken) throws IOException {
        try{
            ResignCommand command = new Gson().fromJson(message, ResignCommand.class);
            GameData gameData = gameDAO.getGame(command.gameID);
            if(gameData.game().state == OVER){
                throw new DataAccessException("Game is already over");
            }
            if((gameData.whiteUsername() == null) && (gameData.blackUsername() == null) || teamColor == null){
                throw new DataAccessException("Observer can't resign");
            }

            AuthTokenData authData = authDAO.getUser(command.getAuthString());
            String username = authData.username();
            game.state = OVER;
            resignError(command.gameID);
            connections.remove(command.gameID, command.getAuthString(), session);
            var messageString = String.format("%s has left the game", username);
            var notification = new NotificationCommand(ServerMessage.ServerMessageType.NOTIFICATION, messageString);
            connections.broadcastLoadToAll(command.gameID, notification, command.getAuthString());
        }
        catch(Exception exception){
            sendError(exception, session);
        }
    }

    private void makeMoveHelper(ChessGame.TeamColor teamColor, ChessGame game, ChessPosition startPos, ChessPosition endPos, Session session) throws InvalidMoveException, DataAccessException, IOException {
        //make move
        //wsHandler = new WebSocketHandler();
        ChessMove finalMove;
        Collection<ChessMove> validMoves = game.validMoves(startPos);
        for (ChessMove move : validMoves) {
            ChessPosition validMove = move.getEndPosition();
            if (validMove.equals(endPos)) {
                //move can be made
                finalMove = new ChessMove(startPos, endPos, null);
                game.makeMove(finalMove);
                break; //the move is made, so end the loop because the valid move was found and made
                //ws.makeMove(authToken, gameID, finalMove);
            }
            else{
                throw new DataAccessException("invalid move");
                //sendError(new Exception("invalid move"), session);
            }

            //implement checkmate, stalemate...etc
        }
    }

    public void resignError(int gameID) throws DataAccessException {
        GameData gameData = gameDAO.getGame(gameID);
        //ChessGame game = gameData.game();
        if (game.state == OVER) {
            throw new DataAccessException("Error: Game is over");
        }
        if ((gameData.whiteUsername() == null) || (gameData.blackUsername() == null)) {
            throw new DataAccessException("Error: White player has resigned");
        }
        //isempty or null?
        if ((gameData.whiteUsername().isEmpty())) {
            throw new DataAccessException("Error: White player has resigned");
        }

        if ((gameData.blackUsername().isEmpty())){
            throw new DataAccessException("Error: Black player has resigned");
        }
    }
    public boolean isIncheckMate() throws DataAccessException {
        if (Objects.equals(teamColor, BLACK)) {
            if (game.isInStalemate(WHITE) || game.isInCheckmate(WHITE)) {
                game.state = OVER;
                throw new DataAccessException("Game is Over");
            }
        }
        else {
            if (game.isInStalemate(BLACK) || game.isInCheckmate(BLACK)) {
                game.state = OVER;
                throw new DataAccessException("Game is Over");
            }
        }
        return true;
    }

    public void sendError(Exception e, Session session) throws IOException {
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
