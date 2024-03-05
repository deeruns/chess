package dataAccess;

import Models.UserData;

import java.sql.SQLException;

public class SqlUserDAO implements UserDAO{

    public SqlUserDAO() throws DataAccessException{
        configureDatabase();
    }
    @Override
    public void clear() throws DataAccessException {

    }

    @Override
    public void createUser(UserData user) throws DataAccessException {

    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        return null;
    }
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
            'username' VARCHAR(255) NOT NULL PRIMARY KEY,
            'password' VARCHAR(255) NOT NULL,
            'email' VARCHAR(255) NOT NULL
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
