package by.touchsoft.vasilyevanatali.Command;


import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UsersAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ExitCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ExitCommand.class);
    private User user;
    private UsersAction usersAction;


    public ExitCommand(User user, UsersAction usersAction) {
        this.user = user;
        this.usersAction = usersAction;
    }


    @Override
    public void execute(String message) {
        if (user.isOnline()) {
            if (user.isInConversation()) {
                LOGGER.info("Client " + user.getName() + " exit from program. " + "Agent " + user.getOpponent().getName() + " become free");
                usersAction.exitUser(user);
                user.disconnectUser();
            }
        }
        if (user.isOnline()) {
            user.disconnectUser();
            LOGGER.info("Client " + user.getName() + " exit from program. ");
        }
    }
}


