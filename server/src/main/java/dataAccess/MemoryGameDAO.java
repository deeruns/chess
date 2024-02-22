package dataAccess;

import Models.GameData;

import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private static HashMap<Integer, GameData> gameDataArray = new HashMap<>();

    public void clear() throws DataAccessException{
        gameDataArray.clear();
    }
}
