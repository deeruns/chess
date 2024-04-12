package server;

import dataAccess.DataAccessException;
import dataAccess.*;
import handlers.*;
import server.websocket.WebSocketHandler;
import spark.*;

public class Server {
    UserDAO userDAO;
    GameDAO gameDAO;
    AuthDAO authDAO;
    private final WebSocketHandler webSocketHandler;
    //add websocket?
    public Server(){
        try {
            this.userDAO = new SqlUserDAO();
            this.authDAO = new SqlAuthDAO();
            this.gameDAO = new SqlGameDAO();
            this.webSocketHandler = new WebSocketHandler();
        } catch (DataAccessException exception) {
            throw new RuntimeException(exception);
        }
    }

    public static void main(String[] args) {
        new Server().run(8080);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");
        Spark.webSocket("/connect", webSocketHandler);
        // Register your endpoints and handle exceptions here.
        endPointRegister();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void endPointRegister(){
        Spark.delete("/db", ((request, response) ->  new ClearHandler().reqHandle(request, response)));
        Spark.post("/user", ((request, response) ->  new RegisterHandler().reqHandle(request, response)));
        Spark.post("/session", ((request, response) ->  new LoginHandler().reqHandle(request, response)));
        Spark.delete("/session", ((request, response) ->  new LogoutHandler().reqHandle(request, response)));
        Spark.get("/game", ((request, response) ->  new ListGamesHandler().reqHandle(request, response)));
        Spark.post("/game", ((request, response) ->  new CreateGameHandler().reqHandle(request, response)));
        Spark.put("/game", ((request, response) ->  new JoinGameHandler().reqHandle(request, response)));
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
