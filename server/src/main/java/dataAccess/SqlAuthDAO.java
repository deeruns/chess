package dataAccess;

import Models.AuthTokenData;
import chess.ChessGame;
import com.google.gson.Gson;

import java.sql.ResultSet;
import java.sql.SQLException;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SqlAuthDAO implements AuthDAO{
    public SqlAuthDAO() throws DataAccessException{
        configureDatabase();
    }
    @Override
    public AuthTokenData getUser(String authToken) throws DataAccessException {
        //returns AuthTokenData
        try (var conn = DatabaseManager.getConnection()){
            var statement = "SELECT authToken, username FROM authTokenTable WHERE authToken=?";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setString(1, authToken);
                try(var rs = prepStatement.executeQuery()){
                    if (rs.next()){
                        return new AuthTokenData(rs.getString("authToken"), rs.getString("username"));
                    }
                    return null;
                }
            }

        }
        catch(SQLException exception){
            throw new DataAccessException("Error: Unauthorized");
        }
    }

    @Override
    public void oneAuthTokenPerPlayer(AuthTokenData authData) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM authTokenTable WHERE authToken = ?";
            try(var prepStatememt = conn.prepareStatement(statement)){
                prepStatememt.setString(1, authData.authToken());
                try(var rs = prepStatememt.executeQuery()){
                    if(rs.next()){
                        deleteAuth(authData.authToken());
                    }
                }
            }
        }
        catch (SQLException exception) {
            throw new DataAccessException("Data Access Exception");
        }
    }

    @Override
    public void createMemory(AuthTokenData authData) throws DataAccessException {
        //put auth into database
        var statement = "INSERT INTO authTokenTable (authToken, username) VALUES (?,?)";
        //var userJson = new Gson().toJson(user);
        DatabaseManager.executeUpdate(statement, authData.authToken(), authData.username());
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
//        var statement = "DELETE FROM authTokenTable WHERE authToken=?";
//        DatabaseManager.executeUpdate(statement, authToken);
            try (var conn = DatabaseManager.getConnection()) {
                var statement = "DELETE FROM authTokenTable WHERE authToken=?";
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.setString(1, authToken);
                    preparedStatement.executeUpdate();
            }
                //throw new DataAccessException("Error: already taken");
            }
            catch (SQLException exception) {
                throw new DataAccessException("Error: cannot log out");
        }
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE authTokenTable";
        DatabaseManager.executeUpdate(statement);
    }
    private AuthTokenData readAuths(ResultSet rs) throws SQLException {
        var authToken = rs.getString("authToken");
        var username= rs.getString("username");
        AuthTokenData authData = new AuthTokenData(authToken, username);
        return new Gson().fromJson(String.valueOf(authData), AuthTokenData.class);
    }
//    private int executeUpdate(String statement, Object... params) throws DataAccessException {
//        try (var conn = DatabaseManager.getConnection()) {
//            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
//                for (var i = 0; i < params.length; i++) {
//                    var param = params[i];
//                    if (param instanceof String p) ps.setString(i + 1, p);
//                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
//                    else if (param instanceof AuthTokenData auth) ps.setString(i + 1, auth.toString());
//                    else if (param == null) ps.setNull(i + 1, NULL);
//                }
//                ps.executeUpdate();
//
//                var rs = ps.getGeneratedKeys();
//                if (rs.next()) {
//                    return rs.getInt(1);
//                }
//
//                return 0;
//            }
//        } catch (SQLException exception) {
//            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, exception.getMessage()));
//            //throw new DataAccessException("Error: already taken");
//        }
//    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authTokenTable (
            `authToken` VARCHAR(255) NOT NULL PRIMARY KEY,
            `username` VARCHAR(255) NOT NULL
            )
            """
    };
    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try(var conn = DatabaseManager.getConnection()){
            for(var statement: createStatements){
                try(var preparedStatement = conn.prepareStatement(statement)){
                    preparedStatement.executeUpdate();
                }
            }

        }
        catch (SQLException exception){
            throw new DataAccessException(String.format("Unable to configure database: %s", exception.getMessage()));
        }
    }
}
