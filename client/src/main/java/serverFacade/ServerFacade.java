package serverFacade;

import ResponseException.ResponseException;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final String servUrl;

    public ServerFacade(String uRl) {
        servUrl = uRl;
    }
    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException{
        try{
            URL url = (new URI(servUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            if(http.getResponseCode() != 200){
                throw new ResponseException(http.getResponseCode(), "Request Has Failed!");
            }
            readbody(http, responseClass);

        }
        catch (Exception exception) {
            throw new ResponseException(500, exception.getMessage());
        }
    }
}
