package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{
    int gameID;
    public static final UserGameCommand.CommandType type = UserGameCommand.CommandType.JOIN_OBSERVER;
    public JoinObserverCommand(String authToken, int gameID){
        super(authToken);
        this.gameID = gameID;
    }

}
