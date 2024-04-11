package webSocketMessages.userCommands;

public class JoinObserverCommand extends UserGameCommand{
    public int gameID;
    public static final UserGameCommand.CommandType type = UserGameCommand.CommandType.JOIN_OBSERVER;
    public JoinObserverCommand(String authToken, int gameID){
        super(authToken, CommandType.JOIN_OBSERVER);
        this.gameID = gameID;
    }

}
