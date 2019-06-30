package by.touchsoft.vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.Message.ChatMessage;
import by.touchsoft.vasilyevanatali.Service.IMessageService;
import by.touchsoft.vasilyevanatali.Service.MessageServiceImpl;
import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UsersAction;
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
     * Variable usersAction for use its methods
     */
    private final UsersAction usersAction;

    /**
     * Variable user what send message to opponent
     */
    private User user;

//    private IMessageService messageService;

    /**
     * Constructor with parameters
     *
     * @param user        - user who send message to opponent
     * @param usersAction - contain method, what using by user
     */
    public ConversationCommand(User user, UsersAction usersAction) {
        this.user = user;
        this.usersAction = usersAction;
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
                    usersAction.addUser(user);
                    user.setInClientCollection(true);
                    LOGGER.info("Client with name " + user.getName() + " has been added to clients queue.");
                    try {
                        usersAction.sendServerMessage("You has been added to client's queue", user);
                        user.getWriter().flush();
                    } catch (IOException e) {
                        LOGGER.error(e);
                        usersAction.exitUser(user);
                        user.disconnectUserByServer();
                    }
                }
                if (user.isInConversation()) {
                    usersAction.sendMessagesHistoryToAgent(user);
                    usersAction.sendMessageToOpponent(user, chatMessage);
                } else {
                    List<ChatMessage> messages = user.getMessages();
                    messages.add(chatMessage);
                }
                break;
            case "AGENT":
                if (!user.isInConversation()) {
                    usersAction.sendServerMessage("Please wait when client write to you", user);
                }
                if (user.getOpponent() != null) {
                    usersAction.sendMessageToOpponent(user, chatMessage);
                }
        }
    }


}
