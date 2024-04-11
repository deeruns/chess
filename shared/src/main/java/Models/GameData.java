package Models;

import chess.ChessGame;
import com.google.gson.JsonArray;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game) {

}
