package by.touchsoft.vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.Message.ChatMessage;
import by.touchsoft.vasilyevanatali.Service.IMessageService;
import by.touchsoft.vasilyevanatali.Service.MessageServiceImpl;
import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UserType;
import by.touchsoft.vasilyevanatali.User.UsersAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public RegisterCommand(User user, UsersAction usersAction) {
        this.user = user;
        this.usersAction = usersAction;
    }

    /**
     * Method handles input messages depending on type of user and register user
     * @param message - register message
     */
    @Override
    public void execute(String message) {
        ChatMessage json=null;
        try {
            json = MessageServiceImpl.INSTANCE.parseFromJson(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String username = json.getSenderName() == null ? "" : json.getSenderName();
        String context = json.getText();
        if (user.isUserExit()) {
            while (!checkFirstMessage(context)) {
                try {
                    ChatMessage answer = new ChatMessage("Server", LocalDateTime.now(), "Please, check you information");
                    user.getWriter().write(MessageServiceImpl.INSTANCE.convertToJson(answer));
                    user.getWriter().newLine();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            String[] splittedFirstMessage = context.split(" ");
            String role = splittedFirstMessage[1];
            user.setRole(UserType.valueOf(role.toUpperCase()));
            user.setName(username);
            user.setUserExit(false);
            switch (user.getRole().toString()) {
                case "AGENT":
                    usersAction.sendServerMessage("Register was successful. Wait when client send you a message", user);
                    usersAction.addUser(user);

                    LOGGER.info("Agent " + user.getName() + " has been registered successful");
                    break;
                case "CLIENT":
                    usersAction.sendServerMessage("Register was successful. Please write you message", user);
                    LOGGER.info("Client " + user.getName() + " has been registered successful");
                    break;
            }
        } else {
            usersAction.sendServerMessage("You are already has been registered", user);
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
