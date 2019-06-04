package by.touchsoft.vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.User.User;


public class ExitCommand implements Command {
    User user;
    String message;
    Server server;


    public ExitCommand(User user, String message, Server server) {
        this.user = user;
        this.message = message;
        this.server = server;
    }

    @Override
    public void execute(String message) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        if (user.isOnline()) {
            if (user.isInConversation()) {
                user.exitUser(user);
                user.disconnectUser();
            }
        }
        if (user.isOnline()) {
            user.disconnectUser();
        }
    }
}


