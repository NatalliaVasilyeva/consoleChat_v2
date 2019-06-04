package by.touchsoft.vasilyevanatali.Command;


import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.User.User;

import java.util.List;

public class ConversationCommand implements Command {
    User user;
    String message;
    Server server;


    public ConversationCommand(User user, String message, Server server) {
        this.user = user;
        this.message = message;
        this.server = server;
    }

    @Override
    public void execute(String message) {

        if (user.isOnline()) {
            if (user.isInConversation()) {
                server.sendMessageToOpponent(user, message);
            } else if (user.getOpponent() == null) {
                if (user.getRole().equals("client")) {
                    List<String> messages = user.getMessages();
                    messages.add(message);
                    if (messages != null && !messages.get(messages.size()-1).equals("/leave")) {
                        for (String offlineMessage : messages) {
                            server.sendMessageToOpponent(user, offlineMessage);
                        }
                        user.getMessages().clear();
                    }
                    server.sendMessageToOpponent(user, message);

                }
                if (user.getRole().equals("agent")) {
                    while (user.getOpponent() == null) {
                        checkClientStatus(user);
                    }
                    server.sendMessageToOpponent(user, message);
                }
            }
        }
    }

    private boolean checkClientStatus(User user) {
        return user.getOpponent() == null;
    }

}
