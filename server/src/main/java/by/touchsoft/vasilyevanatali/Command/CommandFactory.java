package by.touchsoft.vasilyevanatali.Command;


import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UsersAction;

public class CommandFactory {

    private ConversationCommand conversationCommand;
    private ExitCommand exitCommand;
    private LeaveCommand leaveCommand;


    public CommandFactory(User user, UsersAction usersAction) {
        conversationCommand = new ConversationCommand(user, usersAction);
        exitCommand = new ExitCommand(user, usersAction);
        leaveCommand = new LeaveCommand(user, usersAction);
    }


    public void startCommand(String message) {
        switch (message) {
            case "/exit":
                exitCommand.execute(message);
                break;
            case "/leave":
                leaveCommand.execute(message);
                break;
            default:
                conversationCommand.execute(message);
                break;
        }
    }
}
