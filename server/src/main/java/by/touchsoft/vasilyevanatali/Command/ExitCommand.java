package by.touchsoft.vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.User.User;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.List;

public class ExitCommand implements Command {
    User user;
    String message;
    Server server;
    boolean status = true;

    public ExitCommand(User user, String message, Server server) {
        this.user = user;
        this.message = message;
        this.server = server;
    }

    @Override
    public void execute(String message) {
        if (user.isOnline()) {
            if (user.isInConversation()) {
                if (user.getRole().equals("client")){
                    server.removeClient(user);
            }
                if (user.getRole().equals("agent")){
                    server.removeAgent(user);
                }
                server.addAgent(user.getOpponent());
                user.disconnectUser();
            }
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
}
