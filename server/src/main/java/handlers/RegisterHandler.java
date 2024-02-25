package handlers;

import Models.AuthTokenData;
import com.google.gson.Gson;
import dataAccess.DataAccessException;
import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryGameDAO;
import dataAccess.MemoryUserDAO;
import requests.RegisterRequest;
import response.ResponseRecord;
import service.RegisterService;
import spark.Request;
import spark.Response;

import java.util.Objects;

public class RegisterHandler {
    public Object reqHandle(Request request, Response response) throws Exception{

        // call service class
        //get result object and send it back
        //authenticate auth code
        //ClearService gameData = new ClearService(new AuthDAO(),new GameDAO(), new UserDAO());
        //parse json string
        Gson gson = new Gson();
        RegisterRequest regRequest = serializeRequest(request.body());
        RegisterService service = new RegisterService(new MemoryAuthDAO(), new MemoryGameDAO(), new MemoryUserDAO());
        //RegisterResponse registerResponse = new RegisterResponse();
        String finalMessage = "";
        try {
            AuthTokenData authData = service.registerUser(regRequest.getUsername(), regRequest.getPassword(), regRequest.getEmail());
            response.status(200);
            finalMessage =  deserializeResponse(authData);
        }
        catch (DataAccessException exception){
            if (Objects.equals(exception.getMessage(), "Error: bad request")){
                response.status(400);
                ResponseRecord responseRecord = new ResponseRecord(exception.getMessage());
                finalMessage = gson.toJson(responseRecord);
            }
            else if (Objects.equals(exception.getMessage(), "Error: already taken")){
                response.status(403);
                ResponseRecord responseRecord = new ResponseRecord(exception.getMessage());
                finalMessage = gson.toJson(responseRecord);
            }
            else{
                response.status(500);
                ResponseRecord responseRecord = new ResponseRecord(exception.getMessage());
                finalMessage = gson.toJson(responseRecord);
            }
        }
        return finalMessage;
        //return deserializeResponse(authData);
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
