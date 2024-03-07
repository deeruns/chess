package dataAccess;

import Models.UserData;
import com.google.gson.Gson;

import java.sql.SQLException;
import static java.sql.Types.NULL;

public class SqlUserDAO implements UserDAO{

    public SqlUserDAO() throws DataAccessException{
        configureDatabase();
    }
    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE TABLE users";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void createUser(UserData user) throws DataAccessException {
        //call getuser to throw exception?
        try(var conn = DatabaseManager.getConnection()){
            var statement = "INSERT INTO users (username, password, email) VALUES (?,?,?)";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setString(1, user.username());
                prepStatement.setString(2, user.password());
                prepStatement.setString(3, user.email());
                prepStatement.executeUpdate();
            }
        }
        catch(SQLException exception){
            throw new DataAccessException("Error: already taken");
        }
        //var userJson = new Gson().toJson(user);
        //DatabaseManager.executeUpdate(statement, user.username(), user.password(), user.email());
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try(var conn = DatabaseManager.getConnection()){
            var statement = "SELECT * FROM users WHERE username=?";
            try(var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setString(1, username);
                try (var rs = prepStatement.executeQuery()){
                    if(rs.next()){
                        return new UserData(rs.getString("username"), rs.getString("password"), rs.getString("email"));
                    }
                    return null;
                }
            }
        }
        catch (SQLException exception) {
            throw new DataAccessException("Error: Unauthorized");
        }
    }
//    private void executeUpdate(String statement, Object... params) throws DataAccessException {
//        try (var conn = DatabaseManager.getConnection();
//             var prepStatement = conn.prepareStatement(statement)) {
//            for (var i = 0; i < params.length; i++) {
//                var param = params[i];
//                if (param instanceof String p) prepStatement.setString(i + 1, p);
//                else if (param == null) prepStatement.setNull(i + 1, NULL);
//            }
//            prepStatement.executeUpdate(); // Return the result of executeUpdate directly
//        } catch (SQLException exception) {
//            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, exception.getMessage()));
//            //throw new DataAccessException("Error: already taken");
//        }
//    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
            `username` VARCHAR(255) NOT NULL PRIMARY KEY,
            `password` VARCHAR(255) NOT NULL,
            `email` VARCHAR(255) NOT NULL
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
