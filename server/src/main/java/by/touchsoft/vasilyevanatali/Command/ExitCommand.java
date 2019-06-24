package by.touchsoft.vasilyevanatali.Command;


import by.touchsoft.vasilyevanatali.Thread.ConversationHandler;
import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UsersAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.TimeUnit;

/**
 * @author Natali
 * Handles "/exit" message from cliens and agents
 */
public class ExitCommand implements Command {

    /**
     * LOGGER variable to log exit information.
     */
    private static final Logger LOGGER = LogManager.getLogger(ExitCommand.class);

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
    public ExitCommand(User user, UsersAction usersAction) {
        this.user = user;
        this.usersAction = usersAction;
    }

    /**
     * Method handles input exit message depending on type of user, type of user conditional (online or not, get opponent or not)
     *
     * @param message - message what has been sent from user to opponent
     */
    @Override
    public void execute(String message) {
        if (user.isOnline()) {
            if (user.isInConversation()) {
                if (user.getRole().equals("client")) {
                    LOGGER.info("Client " + user.getName() + " exit from program. " + "Agent " + user.getOpponent().getName() + " become free");
                } else {
                    LOGGER.info("Agent " + user.getName() + " exit from program. " + "Client " + user.getOpponent().getName() + " become free");
                }
                usersAction.exitUser(user);
                try {
                    TimeUnit.MILLISECONDS.sleep(400);
                } catch (InterruptedException e) {
                    LOGGER.info("Problem with sleep mode", e);
                }
                user.disconnectUserByServer();
            }
        }
        if (user.isOnline()) {
            user.disconnectUserByServer();
            if (user.getRole().equals("client")) {
                LOGGER.info("Client " + user.getName() + " exit from program. ");
            } else {
                LOGGER.info("Agent " + user.getName() + " exit from program. ");
            }
        }
    }
}


