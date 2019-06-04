package by.touchsoft.vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.User.User;

public class LeaveCommand implements Command {

    User user;
    String message;
    Server server;


    public LeaveCommand(User user, String message, Server server) {
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
                user.disconnectFromAgent(user);
            }
            if (user.isOnline()) {
                user.disconnectFromAgent(user);
            }
        }
    }
}
