package by.touchsoft.vasilyevanatali.Thread;


import by.touchsoft.vasilyevanatali.User.UsersAction;

/**
 * @author Natali
 * Thread that connect client to agent
 */
public class ConnectOpponentThread extends Thread {

    /**
     * Variable usersAction for use its methods
     */
    private final UsersAction usersAction;

    /**
     * Constructor with parameters
     * @param usersAction contain method, what using by user
     */
    public ConnectOpponentThread(UsersAction usersAction) {
        this.usersAction = usersAction;
    }

    /**
     * Start thread to find agent for client
     */
    @Override
    public void run() {
        while (true) {
            usersAction.connectToOpponent();
        }
    }
}



