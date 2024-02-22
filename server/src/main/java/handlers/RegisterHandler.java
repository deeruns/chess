package handlers;

import com.google.gson.Gson;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import service.ClearService;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler {
//    public Object reqHandle(Request request, Response response) throws Exception{
//
//        // call service class
//        //get result object and send it back
//        //authenticate auth code
//        //ClearService gameData = new ClearService(new AuthDAO(),new GameDAO(), new UserDAO());
//        //parse json string
//        Gson gson = new Gson();
//        Request regRequest = (Request)gson.fromJson(String.valueOf(request), Request.class);
//        RegisterService service = new RegisterService();
//        //get result back
//        Response regResponse = service.register(regRequest);
//
//        return gson.toJson(regResponse);
//    }


}
