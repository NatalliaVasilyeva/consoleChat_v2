package by.touchsoft.vasilyevanatali.Command;


import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Service.MessageServiceImpl;

import java.io.IOException;

/**
 * @author Natali
 * Pattern factory for rang input messages
 */

public class CommandFactory {

    /**
     * Variable conversationCommand - for create object of ConversationCommand class
     */
    private final ConversationCommand conversationCommand;

    /**
     * Variable exitCommand - for create object of ExitCommand class
     */
    private final ExitCommand exitCommand;

    /**
     * Variable leaveCommand - for create object of LeaveCommand class
     */
    private final LeaveCommand leaveCommand;

    /**
     * Constructor with parameters. Create objects of all command classes
     *
     * @param user - object of user class. Concrete agent or client
     */
    public CommandFactory(User user) {
        conversationCommand = new ConversationCommand(user);
        exitCommand = new ExitCommand(user);
        leaveCommand = new LeaveCommand(user);
    }


    /**
     * Method call need command based on type of message
     *
     * @param message - message from user what has been send to opponent or server
     */
    public void startCommand(ChatMessage message) {

        String context = message != null ? message.getText() : null;

        String[] splittedFirstMessage = context.split(" ");
        if (splittedFirstMessage.length == 0) {
            return;
        }
        switch (splittedFirstMessage[0]) {
            case "/exit":
                exitCommand.execute(message);
                break;
            case "/leave":
                leaveCommand.execute(message);
                break;
            default:
                conversationCommand.execute(message);
                break;
        }
    }
}
