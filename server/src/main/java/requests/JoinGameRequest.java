package requests;

import chess.ChessGame;

public record JoinGameRequest(String teamColor, int gameID) {
}
