package handlers;

import Models.GameData;
import com.google.gson.Gson;
import dataAccess.*;
import response.CreateGameResponse;
import response.ListGamesResponse;
import service.ListGamesService;
import spark.Request;
import spark.Response;

import java.util.Collection;

public class ListGamesHandler extends ParentHandler {
    public Object reqHandle(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("authorization");
        ListGamesService service = new ListGamesService(new SqlAuthDAO(), new SqlGameDAO());
        String finalResponse = "";
        try {
            Collection<GameData> gamedata = service.listGames(authToken);
            response.status(200);
            ListGamesResponse listGamesResponse = new ListGamesResponse(gamedata);
            finalResponse = deserializeResponse(listGamesResponse);
        } catch (DataAccessException exception) {
            finalResponse = errorMessageGenerator(exception, response);
        }
        return finalResponse;
    }
    private String deserializeResponse(ListGamesResponse listGamesResponse) throws DataAccessException{
        try{
            Gson gson = new Gson();
            return gson.toJson(listGamesResponse);
        }
        catch (Exception exception){
            throw new DataAccessException(exception.getMessage());
        }
    }
}
