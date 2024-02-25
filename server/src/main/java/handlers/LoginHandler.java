package handlers;

import Models.AuthTokenData;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import requests.LoginRequest;
import requests.RegisterRequest;
import response.ResponseRecord;
import service.LoginService;
import service.RegisterService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class LoginHandler extends ParentHandler {
    public Object reqHandle(Request request, Response response) throws Exception{
    Gson gson = new Gson();
    LoginRequest loginRequest = serializeRequest(request.body());
    LoginService service = new LoginService(new MemoryAuthDAO(),new MemoryUserDAO());
    String finalMessage = "";
        try {
        AuthTokenData authData = service.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        response.status(200);
        finalMessage =  deserializeResponse(authData);
    }
        catch (DataAccessException exception){
//        if (Objects.equals(exception.getMessage(), "Error: Unauthorized")){
//            response.status(401);
//            ResponseRecord responseRecord = new ResponseRecord(exception.getMessage());
//            finalMessage = gson.toJson(responseRecord);
//        }
//        else{
//            response.status(500);
//            ResponseRecord responseRecord = new ResponseRecord(exception.getMessage());
//            finalMessage = gson.toJson(responseRecord);
//        }
            //from parent class
            finalMessage = errorMessageGenerator(exception, response);
    }
        return finalMessage;
    //return deserializeResponse(authData);
}

        private LoginRequest serializeRequest(String request){
            Gson gson = new Gson();
            LoginRequest loginRequest = (LoginRequest)gson.fromJson(request, LoginRequest.class);
            return loginRequest;
        }

        private String deserializeResponse(AuthTokenData authToken) throws Exception{
            try{
                Gson gson = new Gson();
                return gson.toJson(authToken);
            }
            catch (Exception exception){
                throw new DataAccessException(exception.getMessage());
            }
        }
}

