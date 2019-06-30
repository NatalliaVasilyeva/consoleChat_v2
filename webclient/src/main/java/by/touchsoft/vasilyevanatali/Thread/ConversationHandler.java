package by.touchsoft.vasilyevanatali.Thread;

import by.touchsoft.vasilyevanatali.Command.CommandFactory;
import by.touchsoft.vasilyevanatali.Command.RegisterCommand;
import by.touchsoft.vasilyevanatali.Message.ChatMessage;
import by.touchsoft.vasilyevanatali.Service.IMessageService;
import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UsersAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author Natali
 * Thread that handlers the messages from user
 */
public class ConversationHandler implements Runnable {

    /**
     * LOGGER variable to log handler information.
     */
    private static final Logger LOGGER = LogManager.getLogger(ConversationHandler.class);

    /**
     * Variable of class usersAction for use its methods
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
    public ConversationHandler(User user, UsersAction usersAction) {
        this.user = user;
        this.usersAction = usersAction;
    }

    /**
     * Method start thread, what read input information and depending on this information call need command from command factory
     */
    @Override
    public void run() {
        try {
            BufferedReader reader = user.getReader();
            while (!user.getSocket().isClosed()) {
                String message = reader.readLine();
                if (message != null) {
                    if (user.isUserExit()) {
                        RegisterCommand registerCommand = new RegisterCommand(user, usersAction);
                        registerCommand.execute(message);
                        continue;
                    }
                    CommandFactory commandFactory = new CommandFactory(user, usersAction);
                    commandFactory.startCommand(message);
                }
            }
        } catch (IOException e) {
            usersAction.exitUser(user);
            user.disconnectUserByServer();
            LOGGER.error("Problem with reading message  " + e.getMessage());
        }
    }
}
