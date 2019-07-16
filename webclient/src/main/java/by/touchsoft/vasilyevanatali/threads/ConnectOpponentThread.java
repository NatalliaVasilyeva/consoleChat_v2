package by.touchsoft.vasilyevanatali.threads;

import by.touchsoft.vasilyevanatali.Service.UserServiceSingleton;

/**
 * @author Natali
 * Thread that connect client to agent
 */
public class ConnectOpponentThread extends Thread {

    /**
     * Variable usersAction for use its methods
     */


    public ConnectOpponentThread() {
    }

    /**
     * Start thread to find agent for client
     */
    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            UserServiceSingleton.INSTANCE.connectToOpponent();
        }
    }
}



