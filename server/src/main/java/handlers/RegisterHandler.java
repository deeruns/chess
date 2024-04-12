package handlers;

import dataAccess.DataAccessException;
import Models.AuthTokenData;
import com.google.gson.Gson;
import dataAccess.*;
import requests.RegisterRequest;
import service.RegisterService;
import spark.Request;
import spark.Response;

public class RegisterHandler extends ParentHandler{
    public Object reqHandle(Request request, Response response) throws Exception{
        Gson gson = new Gson();
        RegisterRequest regRequest = serializeRequest(request.body());
        RegisterService service = new RegisterService(new SqlAuthDAO(), new SqlUserDAO());
        String finalMessage = "";
        try {
            AuthTokenData authData = service.registerUser(regRequest.getUsername(), regRequest.getPassword(), regRequest.getEmail());
            response.status(200);
            finalMessage =  deserializeResponse(authData);
        }
        catch (DataAccessException exception){
            finalMessage = errorMessageGenerator(exception, response);
        }
        return finalMessage;
    }

    private RegisterRequest serializeRequest(String request){
        Gson gson = new Gson();
        RegisterRequest regRequest = (RegisterRequest)gson.fromJson(request, RegisterRequest.class);
        return regRequest;
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
