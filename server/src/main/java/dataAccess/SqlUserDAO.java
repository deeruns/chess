package dataAccess;

import DataAccess.DataAccessException;
import Models.UserData;

import java.sql.SQLException;

public class SqlUserDAO implements UserDAO{

    public SqlUserDAO() throws DataAccessException {
        DatabaseManager.configureDatabase(createStatements);
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
                    else{
                        throw new DataAccessException("Error: Unauthorized");
                    }
                }
            }
        }
        catch (SQLException exception) {
            throw new DataAccessException("Error: Unauthorized");
        }
    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
            `username` VARCHAR(255) NOT NULL PRIMARY KEY,
            `password` VARCHAR(255) NOT NULL,
            `email` VARCHAR(255) NOT NULL
            )
            """
    };
}
