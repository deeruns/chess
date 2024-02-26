package handlers;

import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import service.ListGamesService;
import spark.Request;
import spark.Response;

public class ListGamesHandler extends ParentHandler {
    public Object reqHandle(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("authorization");
        ListGamesService service = new ListGamesService(new MemoryAuthDAO(), new MemoryGameDAO());
        String finalResponse = "";
        try {
            service.listGames(authToken);
            response.status(200);
        } catch (DataAccessException exception) {
            finalResponse = errorMessageGenerator(exception, response);
        }
        return finalResponse;
    }
}
