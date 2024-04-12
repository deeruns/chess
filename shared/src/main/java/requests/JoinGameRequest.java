package requests;

import chess.ChessGame;

import java.util.Objects;

public final class JoinGameRequest {
    private final String playerColor;
    private final int gameID;
    private String auth;

    public JoinGameRequest(String playerColor, int gameID, String auth) {
        this.playerColor = playerColor;
        this.gameID = gameID;
        this.auth = auth;
    }

    public String getAuth() {
        return auth;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }


    public String playerColor() {
        return playerColor;
    }

    public int gameID() {
        return gameID;
    }
}
