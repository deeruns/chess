package handlers;

import DataAccess.DataAccessException;
import com.google.gson.Gson;
import dataAccess.*;
import requests.JoinGameRequest;
import service.JoinGameService;
import spark.Response;
import spark.Request;


public class JoinGameHandler extends ParentHandler {
    public Object reqHandle(Request request, Response response)throws DataAccessException {
        String authToken = request.headers("authorization");
        JoinGameService service = new JoinGameService(new SqlAuthDAO(), new SqlGameDAO(), new SqlUserDAO());
        JoinGameRequest joinGameRequest = serializeRequest(request.body());
        String finalResponse = "{}";
        //errorHandling(request);
        try{
            if (joinGameRequest.playerColor()  == null){
                service.spectatorJoin(joinGameRequest.gameID(), authToken);
            }
            service.joinGame(joinGameRequest.playerColor(), joinGameRequest.gameID(), authToken);
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

}
