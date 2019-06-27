package by.touchsoft.vasilyevanatali.Command;


import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UsersAction;

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
     * @param user - object of user class. Concrete agent or client
     * @param usersAction - contain method what using by user
     */
    public CommandFactory(User user, UsersAction usersAction) {
        registerCommand = new RegisterCommand(user, usersAction);
        conversationCommand = new ConversationCommand(user, usersAction);
        exitCommand = new ExitCommand(user, usersAction);
        leaveCommand = new LeaveCommand(user, usersAction);
          }


    /**
     * Method call need command based on type of message
     * @param message - message from user what has been send to opponent or server
     */
    public void startCommand(String message) {
        String[] splittedFirstMessage = message.split(" ");
        if(splittedFirstMessage.length==0){
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
