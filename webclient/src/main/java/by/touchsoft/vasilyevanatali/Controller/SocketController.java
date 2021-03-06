package by.touchsoft.vasilyevanatali.Controller;


import by.touchsoft.vasilyevanatali.threads.ConnectOpponentThread;
import by.touchsoft.vasilyevanatali.threads.SocketConnectionThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Natali
 * Start listen port and run all threads
 */

@Controller
public class SocketController {
    /**
     * LOGGER variable to log server information.
     */
    private static final Logger LOGGER = LogManager.getLogger(SocketController.class);

    /**
     * PORT variables are constants keeping appropriate information about connection.
     */
    private static final int PORT = 8189;

    /**
     * Variable use to listen the port and accept input socket
     */
    private ServerSocket serverSocket;

    /**
     * Constructor with parameters
     * Start listen the port. Run connectToOpponent thread and Connection thread
     * <p>
     * //     * @param usersAction - variable of class usersAction for use its methods
     */


    public SocketController() {
        LOGGER.info("Server is running ...");
        try {
            serverSocket = new ServerSocket(PORT);
            new Thread(new SocketConnectionThread(serverSocket, this)).start();
        } catch (IOException e) {
            LOGGER.info("Problem with server socket", e);
            disconnectServer();
            throw new RuntimeException(e);
        }
        new ConnectOpponentThread().start();
    }

    /**
     * Method to disconnect server
     */
    private synchronized void disconnectServer() {
        try {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
            }
            System.exit(0);

        } catch (IOException e) {
            LOGGER.error("Problem with disconnecting server");
            System.exit(0);
        }
    }
}

