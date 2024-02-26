package handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import org.eclipse.jetty.http.MetaData;
import requests.RegisterRequest;
import service.LogoutService;
import spark.Response;
import spark.Request;

public class LogoutHandler extends ParentHandler{
    public Object reqHandle(Request request, Response response) throws DataAccessException {
        String authToken = request.headers("authorization");
        LogoutService service = new LogoutService(new MemoryAuthDAO());
        String finalResponse = "{}";
        try{
            service.logoutUser(authToken);
            response.status(200);
        }
        catch(DataAccessException exception){
            finalResponse = errorMessageGenerator(exception, response);
        }
        return finalResponse;
    }
}
