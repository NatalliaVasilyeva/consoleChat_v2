package by.touchsoft.vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.User.User;

public class CommandFactory {

    private ConversationCommand conversationCommand;
    private ExitCommand exitCommand;
    String message;

    public CommandFactory(User user, String message, Server server) {
        conversationCommand = new ConversationCommand(user, message, server);
        exitCommand = new ExitCommand(user, message, server);
        this.message = message;
    }


    public void startCommand(String message) {
        switch (message) {
            case "/exit":
                exitCommand.execute(message);
                break;
            default:
                conversationCommand.execute(message);
                break;
        }
    }
}
