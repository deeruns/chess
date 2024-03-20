package requests;

public class ListGamesRequest {
    private String auth;

    public ListGamesRequest(String auth){
        this.auth = auth;
    }

    public String getAuth() {
        return auth;
    }
}
