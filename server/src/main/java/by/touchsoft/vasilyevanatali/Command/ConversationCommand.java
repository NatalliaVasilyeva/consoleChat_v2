package by.touchsoft.vasilyevanatali.Command;


import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UsersAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;

/**
 * @author Natali
 * Handles input messages from cliens and agents
 */
public class ConversationCommand implements Command {

    /**
     * LOGGER variable to log conversation information.
     */
    private static final Logger LOGGER = LogManager.getLogger(ConversationCommand.class);

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
     * @param user - user who send message to opponent
     * @param usersAction - contain method, what using by user
     */
    public ConversationCommand(User user, UsersAction usersAction) {
        this.user = user;
        this.usersAction = usersAction;
    }


    /**
     * Method handles input messages depending on type of user, type of user conditional (online or not, get opponent or not)
     * if client doesn't have opponent hes messages save into list of messages. After getting opponent all messages will be send to opponent
     * @param message - message what send from user to opponent
     */
    @Override
    public void execute(String message) {
        if (user.isOnline()) {
            if (user.isInConversation()) {
                List<String> messages = user.getMessages();
                if (messages != null) {
                    messages.forEach(offlineMessage -> usersAction.sendMessageToOpponent(user, offlineMessage));
                    user.getMessages().clear();
                }
                usersAction.sendMessageToOpponent(user, message);

            } else if (user.getOpponent() == null) {
                if (user.getRole().equals("client")) {
                    List<String> messages = user.getMessages();
                    messages.add(message);
                } else if (user.getRole().equals("agent")) {
                    while (user.getOpponent() == null) {
                        checkClientStatus(user);
                    }
                }
            }
        } else {
            List<String> messages = user.getMessages();
            user.setOnline(true);
            usersAction.addUser(user);
            LOGGER.info(user.getName() + " connected to new agent");
            if (user.getOpponent() == null) {
                messages.add(message);
            }
            if (messages != null && user.getOpponent() != null) {
                messages.forEach(offlineMessage -> usersAction.sendMessageToOpponent(user, offlineMessage));
                user.getMessages().clear();
            }
        }
    }

    /**
     *
     * @param user - user what send or receive the messages
     * @return true or false. True - when user get or has opponent
     */
    private boolean checkClientStatus(User user) {

        return user.getOpponent() == null;
    }

}
