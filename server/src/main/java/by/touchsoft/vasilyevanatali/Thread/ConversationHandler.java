package by.touchsoft.vasilyevanatali.Thread;

import by.touchsoft.vasilyevanatali.Command.CommandFactory;
import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UsersAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;


public class ConversationHandler implements Runnable {
    private static final Logger LOGGER = LogManager.getLogger(ConversationHandler.class);
    private User user;
    private UsersAction usersAction;


    public ConversationHandler(User user, UsersAction usersAction) {
        this.user = user;
        this.usersAction = usersAction;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = user.getReader();
            while (!user.isUserExit() && !user.getSocket().isClosed()) {
                String message = reader.readLine();
                if (message != null) {
                    CommandFactory commandFactory = new CommandFactory(user, usersAction);
                    commandFactory.startCommand(message);
                }
            }
        } catch (IOException e) {
            LOGGER.error("Problem with reading message  " + e.getMessage());
            user.disconnectUser();

        }
    }


}
