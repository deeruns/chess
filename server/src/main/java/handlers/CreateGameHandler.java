package handlers;

import DataAccess.DataAccessException;
import Models.GameData;
import com.google.gson.Gson;
import dataAccess.*;
import requests.CreateGameRequest;
import response.CreateGameResponse;
import service.CreateGameService;
import spark.Response;
import spark.Request;
public class CreateGameHandler extends ParentHandler{
    public Object reqHandle(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("authorization");
        CreateGameService service = new CreateGameService(new SqlAuthDAO(), new SqlGameDAO());
        CreateGameRequest createGameRequest = serializeRequest(request.body());
        String finalResponse = "";
        try{
            GameData gameData = service.createGame(authToken, createGameRequest.getGameName());
            response.status(200);
            CreateGameResponse createGameResponse = new CreateGameResponse(gameData.gameID());
            finalResponse = deserializeResponse(createGameResponse);
        }
        catch(DataAccessException exception){
            finalResponse = errorMessageGenerator(exception, response);
        }
        return finalResponse;
    }
    private CreateGameRequest serializeRequest(String request){
        Gson gson = new Gson();
        CreateGameRequest createGameRequest = (CreateGameRequest) gson.fromJson(request, CreateGameRequest.class);
        return createGameRequest;
    }

    private String deserializeResponse(CreateGameResponse createGameResponse) throws DataAccessException {
        try{
            Gson gson = new Gson();
            return gson.toJson(createGameResponse);
        }
        catch (Exception exception){
            throw new DataAccessException(exception.getMessage());
        }
    }

}
