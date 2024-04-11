package webSocketMessages.userCommands;

public class LeaveGameCommand extends UserGameCommand {
    public int gameID;
    public LeaveGameCommand(String authToken, int gameID){
        super(authToken, CommandType.LEAVE);
        this.gameID = gameID;
    }
}
