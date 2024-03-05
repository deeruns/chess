package dataAccess;

import Models.AuthTokenData;
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
            var statement = "SELECT authToken, json FROM auths WHERE authToken=?";
            try(var prepStatement = conn.prepareStatement(statement)){

            }
        }
    }

    @Override
    public void oneAuthTokenPerPlayer(AuthTokenData authData) {

    }

    @Override
    public void createMemory(AuthTokenData authData) throws DataAccessException {

    }

    @Override
    public void deleteAuth(String authToken) {

    }

    @Override
    public void clear() throws DataAccessException {

    }
    private AuthTokenData readAuths(ResultSet rs) throws SQLException {
        var id = rs.getInt("id");
        var json = rs.getString("username");
        var AuthData = new Gson().fromJson(json, AuthTokenData.class);
        return AuthData.setId(id);
    }
    private int executeUpdate(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
                for (var i = 0; i < params.length; i++) {
                    var param = params[i];
                    if (param instanceof String p) ps.setString(i + 1, p);
                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
                    else if (param instanceof AuthTokenData auth) ps.setString(i + 1, auth.toString());
                    else if (param == null) ps.setNull(i + 1, NULL);
                }
                ps.executeUpdate();

                var rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }

                return 0;
            }
        } catch (SQLException exception) {
            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, exception.getMessage()));
        }
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auths (
            'authToken' VARCHAR(255) NOT NULL PRIMARY KEY,
            'username' VARCHAR(255) NOT NULL,
            FOREIGN KEY (username) REFERENCES users(username)
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
