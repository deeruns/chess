package server;

import handlers.*;
import spark.*;

public class Server {
    public static void main(String[] args) {
        new Server().run(8080);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        endPointRegister();

        Spark.awaitInitialization();
        return Spark.port();
    }

    private void endPointRegister(){
        Spark.delete("/db", ((request, response) ->  new ClearHandler().reqHandle(request, response)));
        Spark.post("/user", ((request, response) ->  new RegisterHandler().reqHandle(request, response)));
//        Spark.post("/session", ((request, response) ->  new LoginHandler().reqHandle(request, response)));
//        Spark.delete("/session", ((request, response) ->  new LogoutHandler().reqHandle(request, response)));
//        Spark.get("/game", ((request, response) ->  new ListGamesHandler().reqHandle(request, response)));
//        Spark.post("/game", ((request, response) ->  new CreateGameHandler().reqHandle(request, response)));
//        Spark.put("/game", ((request, response) ->  new JoinGameHandler().reqHandle(request, response)));
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
