package by.touchsoft.vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Repository.UserRepository;
import by.touchsoft.vasilyevanatali.Service.MessageServiceImpl;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Service.UserServiceSingleton;
import by.touchsoft.vasilyevanatali.User.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Natali
 * Handles input messages from cliens and agents
 */
public class RegisterCommand implements Command {

    /**
     * LOGGER variable to log conversation information.
     */
    private static final Logger LOGGER = LogManager.getLogger(RegisterCommand.class);


    /**
     * Variable user what send message to opponent
     */
    private User user;

    /**
     * Constructor with parameters
     *
     * @param user - user who send message to opponent
     *
     */
    public RegisterCommand(User user) {
        this.user = user;
    }

    /**
     * Method handles input messages depending on type of user and register user
     *
     * @param message - register message
     */
    @Override
    public void execute(String message) {
        ChatMessage json = null;
        try {
            json = MessageServiceImpl.INSTANCE.parseFromJson(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String username = json.getSenderName() == null ? "" : json.getSenderName();
        String context = json.getText();
        if (user.isUserExit()) {
            while (!checkFirstMessage(context)) {
                UserServiceSingleton.INSTANCE.sendServerMessage("Please, check you information", user);
            }
            String[] splittedFirstMessage = context.split(" ");
            String role = splittedFirstMessage[1];
            user.setRole(UserType.valueOf(role.toUpperCase()));
            user.setName(username);
            user.setUserExit(false);
            switch (user.getRole().toString()) {
                case "AGENT":
                    UserServiceSingleton.INSTANCE.sendServerMessage("Register was successful. Wait when client send you a message", user);
                    UserServiceSingleton.INSTANCE.addUser(user);
                    UserRepository.INSTANCE.addUser(user);
                    LOGGER.info("Agent " + user.getName() + " has been registered successful");
                    break;
                case "CLIENT":
                    UserServiceSingleton.INSTANCE.sendServerMessage("Register was successful. Please write you message", user);
                    UserRepository.INSTANCE.addUser(user);
                    LOGGER.info("Client " + user.getName() + " has been registered successful");
                    break;
            }
        } else {
            UserServiceSingleton.INSTANCE.sendServerMessage("You are already has been registered", user);
        }
    }

    /**
     * Method help to check information about user. If information is bad - ask to repeat message
     *
     * @param message - input first message from client
     * @return true or false. False - when message is wrong
     */
    private boolean checkFirstMessage(String message) {
        Matcher matcher = Pattern.compile("/reg (client|agent) [A-z]+").matcher(message);
        String userMessage = null;
        if (matcher.find()) {
            userMessage = matcher.group(0);
        }
        return message.equals(userMessage);
    }

}
