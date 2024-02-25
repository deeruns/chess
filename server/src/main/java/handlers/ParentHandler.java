package handlers;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import requests.RegisterRequest;
import response.ResponseRecord;
import spark.Response;

import java.util.Objects;

public class ParentHandler {
    public String errorMessageGenerator(DataAccessException exception, Response response) {
        Gson gson = new Gson();
        String finalMessage = "";
        if (Objects.equals(exception.getMessage(), "Error: bad request")) {
            response.status(400);
            ResponseRecord responseRecord = new ResponseRecord(exception.getMessage());
            finalMessage = gson.toJson(responseRecord);
        }
        else if (Objects.equals(exception.getMessage(), "Error: already taken")) {
            response.status(403);
            ResponseRecord responseRecord = new ResponseRecord(exception.getMessage());
            finalMessage = gson.toJson(responseRecord);
        }
        else if (Objects.equals(exception.getMessage(), "Error: Unauthorized")){
            response.status(401);
            ResponseRecord responseRecord = new ResponseRecord(exception.getMessage());
            finalMessage = gson.toJson(responseRecord);
        }
        else {
            response.status(500);
            ResponseRecord responseRecord = new ResponseRecord(exception.getMessage());
            finalMessage = gson.toJson(responseRecord);
        }
        return finalMessage;
    }
//    public RegisterRequest serializeRequest(String request){
//        Gson gson = new Gson();
//        RegisterRequest regRequest = (RegisterRequest)gson.fromJson(request, RegisterRequest.class);
//        return regRequest;
//    }

}
