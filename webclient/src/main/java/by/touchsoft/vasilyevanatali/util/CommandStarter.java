package by.touchsoft.vasilyevanatali.util;

import by.touchsoft.vasilyevanatali.Command.ConversationCommand;
import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Model.User;

import java.time.LocalDateTime;

/**
 * @author Natali
 * Class start conversation command for rest client
 */
public class CommandStarter {

    /**
     * Method create message and start conversation
     * @param name - user name
     * @param user - user
     * @param message - message
     */
    public void commandStarterInRestController(String name, User user, String message) {
        ChatMessage chatMessage = new ChatMessage(name, LocalDateTime.now(), message);
            ConversationCommand conversationCommand = new ConversationCommand(user);
            conversationCommand.execute(chatMessage);

    }
}
