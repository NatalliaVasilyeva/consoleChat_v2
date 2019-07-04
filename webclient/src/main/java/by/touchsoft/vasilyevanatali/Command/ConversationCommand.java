package by.touchsoft.vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Service.MessageServiceImpl;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Service.UserServiceSingleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
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
     * Variable user what send message to opponent
     */
    private User user;

    /**
     * Constructor with parameters
     *
     * @param user        - user who send message to opponent
     */
    public ConversationCommand(User user) {
        this.user = user;
    }


    /**
     * Method handles input messages depending on type of user, type of user conditional (online or not, get opponent or not)
     * if client doesn't have opponent hes messages save into list of messages. After getting opponent all messages will be send to opponent
     *
     * @param message - message what send from user to opponent
     */
    @Override
    public void execute(String message) {
        ChatMessage chatMessage = null;
        try {
            chatMessage = MessageServiceImpl.INSTANCE.parseFromJson(message);
        } catch (IOException e) {
            LOGGER.error(e);
        }

        switch (user.getRole().toString()) {
            case "CLIENT":
                if (!user.isInClientCollection()) {
                    UserServiceSingleton.INSTANCE.addUser(user);
                    user.setInClientCollection(true);
                    LOGGER.info("Client with name " + user.getName() + " has been added to clients queue.");
                    if(!user.isRestClient()) {
                        UserServiceSingleton.INSTANCE.sendServerMessage("You has been added to client's queue", user);
                    }
                }
                if (user.isInConversation()) {
                //    UserServiceSingleton.INSTANCE.sendMessagesHistoryToAgent(user);
                    UserServiceSingleton.INSTANCE.sendMessageToOpponent(user, chatMessage);
                } else {
                    List<ChatMessage> messages = user.getMessages();
                    messages.add(chatMessage);
                }
                break;
            case "AGENT":
                if (!user.isInConversation()) {
                    if(!user.isRestClient()) {
                        UserServiceSingleton.INSTANCE.sendServerMessage("Please wait when client write to you", user);
                    }
                }
                if (user.getOpponent() != null) {
                    UserServiceSingleton.INSTANCE.sendMessageToOpponent(user, chatMessage);
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + user.getRole().toString());
        }
    }


}
