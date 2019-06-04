package by.touchsoft.vasilyevanatali.Thread;

import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.User.User;

public class ConnectOponnentThread extends Thread {
    private Server server;
    private User user;


    public ConnectOponnentThread(Server server, User user) {
        this.server = server;
        this.user = user;
    }

    @Override
    public void run() {
        User opponent;

        while (!user.isUserExit() && user.getOpponent() == null) {
            opponent = server.getAgent();

            if (opponent != null) {
                user.setOpponent(opponent);
                opponent.setOpponent(user);
                server.sendMessageToOpponent(user, "Client is connected");
                server.sendMessageToOpponent(opponent, "Agent is connected");
                opponent.setInConversation(true);
                user.setInConversation(true);
            }
        }
    }
}



