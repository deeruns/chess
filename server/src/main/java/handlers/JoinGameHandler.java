package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import service.JoinGameService;
import spark.Response;
import spark.Request;


public class JoinGameHandler extends ParentHandler {
    public Object reqHandle(Request request, Response response)throws DataAccessException{
        String authToken = request.headers("authorization");
        JoinGameService service = new JoinGameService(new MemoryAuthDAO(), new MemoryGameDAO(), new MemoryUserDAO());
        JoinGameRequest joinGameRequest = serializeRequest(request.body());
        String finalResponse = "";
        //check if gameID is null and spectator
        errorHandling(request);
        try{
            if (joinGameRequest.teamColor() == null){
                service.spectatorJoin(joinGameRequest.gameID(), authToken);
            }
            service.joinGame(joinGameRequest.teamColor(), joinGameRequest.gameID(), authToken);
            response.status(200);
        }
        catch (DataAccessException exception){
            finalResponse = errorMessageGenerator(exception, response);
        }
        return finalResponse;
    }
    private JoinGameRequest serializeRequest(String request){
        Gson gson = new Gson();
        JoinGameRequest joinGameRequest = (JoinGameRequest) gson.fromJson(request, JoinGameRequest.class);
        return joinGameRequest;
    }
    private void errorHandling(Request request) throws DataAccessException{
        JsonObject json = JsonParser.parseString(request.body()).getAsJsonObject();
        if (!json.has("gameID") || json.get("gameID").isJsonNull()) {
            throw new DataAccessException("Error: bad request");
        }
    }
}
