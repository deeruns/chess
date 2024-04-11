package serverFacade;

import DataAccess.DataAccessException;
import Models.AuthTokenData;
import ResponseException.ResponseException;
import com.google.gson.Gson;
import requests.*;
import response.CreateGameResponse;
import response.ListGamesResponse;
import response.ResponseRecord;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Objects;

//call websocket and HTTP communicator

//UNSTATic everything?

//ServerMessageObserver passed in as constrictor parameter


public class ServerFacade {
    private static String servUrl;

    public ServerFacade(String uRl) {
        servUrl = uRl;
    }
    public void clear() throws DataAccessException {
        var path = "/db";
        this.makeRequest("DELETE", path, null , ResponseRecord.class, null);
    }
    public AuthTokenData register(RegisterRequest request) throws DataAccessException {
        var path = "/user";
        return this.makeRequest("POST", path, request, AuthTokenData.class, null);
    }
    public AuthTokenData login(LoginRequest request) throws DataAccessException {
        var path = "/session";
        return this.makeRequest("POST", path, request, AuthTokenData.class, null);
    }
    public ListGamesResponse listGames(ListGamesRequest request) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("GET", path, request, ListGamesResponse.class, request.getAuth());
    }
     public void logout(LogoutRequest request) throws DataAccessException{
         var path = "/session";
         this.makeRequest("DELETE", path, request, null, request.getAuth());
     }
     public CreateGameResponse createGame(CreateGameRequest request) throws DataAccessException{
         var path = "/game";
         return this.makeRequest("POST", path, request, CreateGameResponse.class, request.getAuth());
     }
     public ResponseRecord joinGame(JoinGameRequest request)throws DataAccessException{
         var path = "/game";
         return this.makeRequest("PUT", path, request, ResponseRecord.class, request.getAuth());
     }

    public static <T> T makeRequest(String method, String path, Object request, Class<T> responseClass, String auth) throws DataAccessException {
        try {
            URL url = (new URI(servUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);

            if (auth != null){
                http.addRequestProperty("authorization", auth);
            }

            if (!Objects.equals(method, "GET")){
                //only use this if there is output to write
                http.setDoOutput(true);
                writeBody(request, http);
            }
            http.connect();
            throwIfNotSuccessful(http);
            return readbody(http, responseClass);

        } catch (Exception exception) {
            throw new DataAccessException(exception.getMessage());
        }
    }

    private static void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException, DataAccessException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respBody = http.getErrorStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);

                ResponseRecord response = new Gson().fromJson(reader, ResponseRecord.class);
                throw new DataAccessException("failure: " + response.message());
            }

        }
    }

    private static boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String requestData = new Gson().toJson(request);
            try (OutputStream requestBody = http.getOutputStream()) {
                requestBody.write(requestData.getBytes());
            }
        }
    }

    private static <T> T readbody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
