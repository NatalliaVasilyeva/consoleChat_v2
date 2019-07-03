package by.touchsoft.vasilyevanatali.Command;


import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Service.MessageServiceImpl;
import by.touchsoft.vasilyevanatali.Model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Natali
 * Pattern factory for rang input messages
 */

public class CommandFactory {

    /**
     * Variable registerCommand - for create object of RegisterCommand class
     */
    private final RegisterCommand registerCommand;

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
        registerCommand = new RegisterCommand(user);
        conversationCommand = new ConversationCommand(user);
        exitCommand = new ExitCommand(user);
        leaveCommand = new LeaveCommand(user);
    }


    /**
     * Method call need command based on type of message
     *
     * @param message - message from user what has been send to opponent or server
     */
    public void startCommand(String message) {
        ChatMessage json = null;
        try {
            json = MessageServiceImpl.INSTANCE.parseFromJson(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String context = json != null ? json.getText() : null;

        String[] splittedFirstMessage = context.split(" ");
        if (splittedFirstMessage.length == 0) {
            return;
        }
        switch (splittedFirstMessage[0]) {
            case "/reg":
                registerCommand.execute(message);
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
