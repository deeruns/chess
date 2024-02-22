package dataAccess;

import Models.GameData;
import chess.ChessGame;

import java.util.Collection;
import java.util.List;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class MemoryGameDAO implements GameDAO{
    private int countID = 1;
    private static HashMap<Integer, GameData> gameDataHash = new HashMap<>();


    public void clear() throws DataAccessException{
        gameDataHash.clear();
    }
}
