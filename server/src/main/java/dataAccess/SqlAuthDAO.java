package dataAccess;

import Models.AuthTokenData;

import java.sql.SQLException;

public class SqlAuthDAO implements AuthDAO{
    public SqlAuthDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
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
                    else {
                        throw new DataAccessException("Error: Unauthorized");
                    }
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
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM authTokenTable WHERE authToken=?";
            try (var preparedStatement = conn.prepareStatement(statement)) {
                preparedStatement.setString(1, authToken);
                int rowCount = preparedStatement.executeUpdate();
                if (rowCount == 0) {
                    throw new DataAccessException("Error: bad request");
                }
            }
        } catch (SQLException exception) {
            throw new DataAccessException("Error: bad request");
        }
    }


    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE authTokenTable";
        DatabaseManager.executeUpdate(statement);
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS authTokenTable (
            `authToken` VARCHAR(255) NOT NULL PRIMARY KEY,
            `username` VARCHAR(255) NOT NULL
            )
            """
    };

}
