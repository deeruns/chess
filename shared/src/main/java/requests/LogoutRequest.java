package requests;

public class LogoutRequest {
    private String auth;

    public LogoutRequest(String auth){
        this.auth = auth;
    }

    public String getAuth() {
        return auth;
    }
}
