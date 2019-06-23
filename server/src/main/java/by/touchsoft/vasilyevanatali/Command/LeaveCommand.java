package by.touchsoft.vasilyevanatali.Command;


import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UsersAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Natali
 * Handles "/leave" message from cliens
 */
public class LeaveCommand implements Command {

    /**
     * LOGGER variable to log leave information.
     */
    private static final Logger LOGGER = LogManager.getLogger(LeaveCommand.class);

    /**
     * Variable usersAction for use its methods
     */
    private final UsersAction usersAction;

    /**
     * Variable user what send message to opponent
     */
    private User user;

    /**
     * Constructor with parameters
     *
     * @param user        - user who send message to opponent
     * @param usersAction - contain method, what using by user
     */
    public LeaveCommand(User user, UsersAction usersAction) {
        this.user = user;
        this.usersAction = usersAction;
    }

    /**
     * Method handles input /leave message depending on type of user conditional (online or not, get opponent or not)
     *
     * @param message - message what has been sent from user to opponent
     */
    @Override
    public void execute(String message) {
        if (user.isOnline()) {
            if (user.isInConversation()) {
                usersAction.disconnectFromAgent(user);
                LOGGER.info("Client " + user.getName() + " has left the chat.");
            }
            if (user.isOnline()) {
                LOGGER.info("Client " + user.getName() + " has left the chat.");
            }
        }
    }
}
