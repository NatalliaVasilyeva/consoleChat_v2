package by.touchsoft.vasilyevanatali.Thread;

import by.touchsoft.vasilyevanatali.Command.CommandFactory;
import by.touchsoft.vasilyevanatali.Command.RegisterCommand;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Service.UserServiceSingleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;

/**
 * @author Natali
 * Thread that handlers the messages from user
 */
public class ConversationHandlerThread implements Runnable {

    /**
     * LOGGER variable to log handler information.
     */
    private static final Logger LOGGER = LogManager.getLogger(ConversationHandlerThread.class);


    /**
     * Variable user what send message to opponent
     */
    private User user;


    /**
     * Constructor with parameters
     *
     * @param user        - user who send message to opponent
     */
    public ConversationHandlerThread(User user) {
        this.user = user;
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
                        RegisterCommand registerCommand = new RegisterCommand(user);
                        registerCommand.execute(message);
                        continue;
                    }
                    CommandFactory commandFactory = new CommandFactory(user);
                    commandFactory.startCommand(message);
                }
            }
        } catch (IOException e) {
            UserServiceSingleton.INSTANCE.exitUser(user);
            user.disconnectUserByServer();
            LOGGER.error("Problem with reading message  " + e.getMessage());
        }
    }
}
