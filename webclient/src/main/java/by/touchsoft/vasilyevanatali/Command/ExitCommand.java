package by.touchsoft.vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Service.UserServiceSingleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

/**
 * @author Natali
 * Handles "/exit" message from cliens and agents
 */
public class ExitCommand implements Command {

    /**
     * LOGGER variable to log exit information.
     */
    private static final Logger LOGGER = LogManager.getLogger(ExitCommand.class);

     /**
     * Variable user what send message to opponent
     */
    private User user;


    /**
     * Constructor with parameters
     *
     * @param user        - user who send message to opponent
     */
    public ExitCommand(User user) {
        this.user = user;
    }

    /**
     * Method handles input exit message depending on type of user, type of user conditional (online or not, get opponent or not)
     *
     *  @param message - message what has been sent from user to opponent
     */
    @Override
    public void execute(String message) {
        UserServiceSingleton.INSTANCE.exitUser(user);
        closeUserSocket(user);
    }

    /**
     *  Method what close socket when user exit
     * @param user - user who send message to opponent
     */

    private void closeUserSocket(User user){
        if(user.getSocket()!=null && !user.getSocket().isClosed()){
            try {
                user.getWriter().close();
                user.getReader().close();
                user.getSocket().close();
            } catch (IOException | NullPointerException e) {
                LOGGER.error("Problem with close socket "+ e.getMessage());
            }
        }
    }
}


