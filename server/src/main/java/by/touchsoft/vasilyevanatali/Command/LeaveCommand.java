package by.touchsoft.vasilyevanatali.Command;


import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UsersAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LeaveCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(LeaveCommand.class);

    private User user;
    private UsersAction usersAction;



    public LeaveCommand(User user, UsersAction usersAction) {
        this.user = user;
        this.usersAction = usersAction;
    }

    @Override
    public void execute(String message) {

        if (user.isOnline()) {
            if (user.isInConversation()) {
               usersAction.disconnectFromAgent(user);
//                LOGGER.info("Client " + user.getName() + " leave chat. " + "Agent " + user.getOpponent().getName() + " become free");
            }
            if (user.isOnline()) {
                usersAction.disconnectFromAgent(user);
                LOGGER.info("Client " + user.getName() + " leave chat.");
            }
        }
    }
}
