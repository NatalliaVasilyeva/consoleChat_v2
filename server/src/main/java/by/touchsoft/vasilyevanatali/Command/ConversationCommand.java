package by.touchsoft.vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.ConversationHandler;
import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.User.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class ConversationCommand implements Command {
    User user;
    String message;
    Server server;
    boolean status = true;
    

    public ConversationCommand(User user, String message, Server server) {
        this.user = user;
        this.message = message;
        this.server = server;
    }

    @Override
    public void execute(String message) {
        if (user.isOnline()) {
            if (user.isInConversation()) {
                if (user.getRole().equals("client")) {
                    System.out.println("common message");
                    sendMessageToOpponent(user, message);
                }
                if (user.getRole().equals("agent")) {
                    sendMessageToOpponent(user, message);
                }
            } else if (user.getOpponent() == null) {
                if (user.getRole().equals("client")) {
                    user.findOpponent(user); // doesn't work!!!!
                    List<String> messages = user.getMessages();
                    for (String offlineMessage : messages) {
                        sendMessageToOpponent(user, offlineMessage);
                    }
                    user.getMessages().clear();
                    sendMessageToOpponent(user, message);
                }
                if (user.getRole().equals("agent")) {
                    while (status) {
                        checkClientStatus(user);
                    }
                    sendMessageToOpponent(user, message);
                }
            }
        } else {
            user.addMessages(message);
        }
    }

    private void sendMessageToOpponent(User user, String message) {
        try (BufferedWriter writer = user.getOpponent().getWriter()) {
            writer.write(message);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean checkClientStatus(User user) {
        return user.getOpponent() == null;
    }

}
