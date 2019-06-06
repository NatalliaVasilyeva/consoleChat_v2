package by.touchsoft.vasilyevanatali.Command;


import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UsersAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class ConversationCommand implements Command {
    private static final Logger LOGGER = LogManager.getLogger(ConversationCommand.class);
    private User user;
    private UsersAction usersAction;


    public ConversationCommand(User user, UsersAction usersAction) {
        this.user = user;
        this.usersAction = usersAction;
    }


    @Override
    public void execute(String message) {
        if (user.isOnline()) {
            if (user.isInConversation()) {
                List<String> messages = user.getMessages();
                if (messages != null) {
                    messages.forEach(offlineMessage -> usersAction.sendMessageToOpponent(user, offlineMessage));
                    user.getMessages().clear();
                }
                usersAction.sendMessageToOpponent(user, message);

            } else if (user.getOpponent() == null) {
                if (user.getRole().equals("client")) {
                    List<String> messages = user.getMessages();
                    messages.add(message);
                } else if (user.getRole().equals("agent")) {
                    while (user.getOpponent() == null) {
                        checkClientStatus(user);
                    }
                }
            }
        } else {
            List<String> messages = user.getMessages();
            user.setOnline(true);
            usersAction.addUser(user);
            LOGGER.info(user.getName() + " come back to chat after leave");
            if (user.getOpponent() == null) {
                messages.add(message);
            }

            if (messages != null && user.getOpponent() != null) {
                messages.forEach(offlineMessage -> usersAction.sendMessageToOpponent(user, offlineMessage));
                user.getMessages().clear();
            }
        }

    }


    private boolean checkClientStatus(User user) {
        return user.getOpponent() == null;
    }

}
