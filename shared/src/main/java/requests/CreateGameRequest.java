package requests;

public class CreateGameRequest {
    private String gameName;
    private String auth;


    public CreateGameRequest(String gameName, String auth){
        this.gameName = gameName;
        this.auth = auth;
    }
    public String getAuth() {
        return auth;
    }
    public String getGameName() {
        return gameName;
    }
}
