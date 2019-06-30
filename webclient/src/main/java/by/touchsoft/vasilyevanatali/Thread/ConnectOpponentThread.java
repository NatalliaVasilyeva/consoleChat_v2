package by.touchsoft.vasilyevanatali.Thread;

import by.touchsoft.vasilyevanatali.User.UserActionSingleton;

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
    @Override
    public void run() {
        while (true) {
            UserActionSingleton.INSTANCE.connectToOpponent();
        }
    }
}



