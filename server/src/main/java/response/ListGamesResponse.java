package response;

import Models.GameData;

import java.util.ArrayList;
import java.util.Collection;

public record ListGamesResponse(Collection<GameData> games) {
}
