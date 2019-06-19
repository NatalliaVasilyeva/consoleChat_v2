package by.touchsoft.vasilyevanatali;


import by.touchsoft.vasilyevanatali.Thread.ConnectOpponentThread;
import by.touchsoft.vasilyevanatali.Thread.Connection;
import by.touchsoft.vasilyevanatali.User.UsersAction;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;


public class Server {
    private static final Logger LOGGER = LogManager.getLogger(Server.class);
    private static final int PORT = 8189;
    private ServerSocket serverSocket;
    private UsersAction usersAction;


    public Server(UsersAction usersAction) {
        System.out.println("Server is running ...");
        LOGGER.info("Server is running ...");
        new ConnectOpponentThread(usersAction).start();
        try {
            serverSocket = new ServerSocket(PORT);
            this.usersAction = usersAction;
            new Thread(new Connection(serverSocket, this)).start();
        } catch (IOException e) {
            LOGGER.error("Problem with server socket");
            disconnectServer();
            throw new RuntimeException(e);
        }
    }


    private synchronized void disconnectServer() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            System.exit(0);
        } catch (IOException e) {
            LOGGER.error("Problem with disconnecting server");
            System.exit(0);
        }
    }

    public UsersAction getUsersAction() {
        return usersAction;
    }
}

