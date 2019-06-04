package by.touchsoft.vasilyevanatali;

import by.touchsoft.vasilyevanatali.Command.CommandFactory;
import by.touchsoft.vasilyevanatali.User.User;

import java.io.BufferedReader;
import java.io.IOException;

public class ConversationHandler implements Runnable {
    User user;
    Server server;


    public ConversationHandler(User user, Server server) {
        this.user = user;
        this.server = server;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = user.getReader();
            if (!user.isOnline()) {
                int size = user.getMessages().size();
                if (user.getMessages().get(size - 1).equals("/leave")) {
                    user.setOnline(true);
                }
            }
            while (!user.isUserExit()) {
                String message = reader.readLine();
                if (message != null) {
                    CommandFactory commandFactory = new CommandFactory(user, message, server);
                    commandFactory.startCommand(message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
