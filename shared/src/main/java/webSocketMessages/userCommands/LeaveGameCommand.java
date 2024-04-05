package webSocketMessages.userCommands;

public class LeaveGameCommand extends UserGameCommand {
    int gameID;
    public LeaveGameCommand(String authToken, int gameID){
        super(authToken);
        this.gameID = gameID;
    }
}
