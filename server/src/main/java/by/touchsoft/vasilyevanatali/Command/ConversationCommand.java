package by.touchsoft.vasilyevanatali.Command;


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
                if (user.getMessages() != null) {

                    server.sendMessageToOpponent(user, message);
                } else if (user.getOpponent() == null) {
                    if (user.getRole().equals("client")) {
                        List<String> messages = user.getMessages();
                        messages.add(message);
                        user.findOpponent(user);
                        if (messages != null) {
                            for (String offlineMessage : messages) {
                                server.sendMessageToOpponent(user, offlineMessage);
                            }
                            user.getMessages().clear();
                        }
                        server.sendMessageToOpponent(user, message);

                    }
                    if (user.getRole().equals("agent")) {
                        while (status) {
                            checkClientStatus(user);
                        }
//                    sendMessageToOpponent(user, message);
//                }
                    }
                }
            }
        }

//    private void sendMessageToOpponent(User user, String message) {
//        try {
//            BufferedWriter writer = user.getOpponent().getWriter();
//            writer.write(message);
//            writer.flush();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//
    }
    private boolean checkClientStatus(User user) {
        return user.getOpponent() == null;
    }

}
