package by.touchsoft.vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Service.UserServiceSingleton;
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
     * Variable user what send message to opponent
     */
    private User user;


    /**
     * Constructor with parameters
     *
     * @param user - user who send message to opponent
     */
    public LeaveCommand(User user) {
        this.user = user;
    }

    /**
     * Method handles input /leave message depending on type of user conditional (online or not, get opponent or not)
     *
     * @param message - message what has been sent from user to opponent
     */
    @Override
    public void execute(ChatMessage message) {
        switch (user.getRole().toString()) {
            case "CLIENT":
                if (user.isInConversation()) {
                    UserServiceSingleton.INSTANCE.disconnectFromAgent(user);
                    LOGGER.info("Client " + user.getName() + " has left the chat.");
                } else {
                    LOGGER.info("Client " + user.getName() + " is not in a chat.");
                }
                break;
            case "AGENT":
                UserServiceSingleton.INSTANCE.sendServerMessage("You can't leave chat. Please, write /exit to exit from chat", user);
        }
    }
}
