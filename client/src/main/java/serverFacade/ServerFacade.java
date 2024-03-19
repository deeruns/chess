package serverFacade;

import ResponseException.ResponseException;
import com.google.gson.Gson;
import com.sun.net.httpserver.Request;
import requests.CreateGameRequest;
import requests.JoinGameRequest;
import requests.LoginRequest;
import requests.RegisterRequest;
import response.CreateGameResponse;
import response.ListGamesResponse;
import response.ResponseRecord;
import service.RegisterService;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final String servUrl;

    public ServerFacade(String uRl) {
        servUrl = uRl;
    }
    public void clear() throws ResponseException {
        var path = "/db";
    }
    public ResponseRecord register(RegisterRequest request) throws ResponseException {
        var path = "/session";
    }
    public ResponseRecord login(LoginRequest request) throws ResponseException {

    }
    public ListGamesResponse listGames(Request request) throws ResponseException {

    }
     public ResponseRecord logout(Request request) throws ResponseException{

     }
     public CreateGameResponse createGame(CreateGameRequest request) throws ResponseException{

     }
     public ResponseRecord joinGame(JoinGameRequest request)throws ResponseException{

     }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(servUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readbody(http, responseClass);

        } catch (Exception exception) {
            throw new ResponseException(500, exception.getMessage());
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }

    private void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String requestData = new Gson().toJson(request);
            try (OutputStream requestBody = http.getOutputStream()) {
                requestBody.write(requestData.getBytes());
            }
        }
    }

    private <T> T readbody(HttpURLConnection http, Class<T> responseClass) throws IOException {
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
