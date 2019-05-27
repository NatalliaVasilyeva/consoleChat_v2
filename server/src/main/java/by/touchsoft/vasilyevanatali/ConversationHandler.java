package by.touchsoft.vasilyevanatali;

import by.touchsoft.vasilyevanatali.Command.CommandFactory;
import by.touchsoft.vasilyevanatali.User.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
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
        try (BufferedReader reader = user.getReader()) {
            while (!user.isUserExit()) {
                String message = reader.readLine();
                if (message != null) {
                    CommandFactory commandFactory = new CommandFactory(user, message, server);
                    commandFactory.startCommand(message);
//                    if (user.getRole().equals("client")) {
//                        switch (message) {
//                            case "/exit":
//                                exit(user);
//                                break;
//                            case "/leave":
//                                disconnectFromAgent(user);
//                                break;
//                            default:
//                                System.out.println("common message");
//                                if (user.getOpponent() == null) {
//                                    user.findOpponent(user); // doesn't work!!!!
//                                    sendMessageToOpponent(user, message);
//                                } else {
//                                    sendMessageToOpponent(user, message);
//                                }
//                                break;
//                        }
//                    }
//                } else {
//                    //agent wait while client make connect to him
//                    while (status) {
//                        checkClientStatus(user);
//                    }
//                    sendMessageToOpponent(user, message);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
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

//    private void exit(User user) {
//        server.removeClient(user);
//        server.addAgent(user.getOpponent());
//        user.disconnectUser();
//    }

    private void disconnectFromAgent(User user) {
        user.getOpponent().setOpponent(null);
        server.addAgent(user.getOpponent());
        user.setOpponent(null);


    }
}
