package handlers;

import dataAccess.*;
import service.ClearService;
import spark.Request;
import spark.Response;

public class ClearHandler {
    public Object reqHandle(Request request, Response response) throws Exception{
        ClearService gameData = new ClearService(new SqlAuthDAO(), new SqlGameDAO(), new SqlUserDAO());
        gameData.clearData();

        response.status(200);
        return "";
    }
}
