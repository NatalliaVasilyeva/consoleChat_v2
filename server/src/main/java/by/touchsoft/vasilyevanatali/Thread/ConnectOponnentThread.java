package by.touchsoft.vasilyevanatali.Thread;

import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.User.User;

public class ConnectOponnentThread extends Thread {
    private Server server;
    private User user;
    Object monitor = new Object();


    public ConnectOponnentThread(Server server, User user) {
        this.server = server;
        this.user = user;
    }

    @Override
    public void run() {
        User opponent = null;
        synchronized (monitor) {

            while (!user.isUserExit() && user.getOpponent() == null) {
                //  if (user.getRole().equals("client")) {
                try {
                    opponent = server.getAgents().take();
                    System.out.println("opponent " + opponent.getName());

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
//                } else if (user.getRole().equals("agent")) {
//                    continue;
//                }
//            }
                //       }

                System.out.println("im here");
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
}


