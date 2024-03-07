package handlers;

import Models.AuthTokenData;
import com.google.gson.Gson;
import dataAccess.*;
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
    LoginService service = new LoginService(new SqlAuthDAO(),new SqlUserDAO());
    String finalMessage = "";
        try {
        AuthTokenData authData = service.loginUser(loginRequest.getUsername(), loginRequest.getPassword());
        response.status(200);
        finalMessage =  deserializeResponse(authData);
    }
        catch (DataAccessException exception){
            finalMessage = errorMessageGenerator(exception, response);
    }
        return finalMessage;
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

