package by.touchsoft.vasilyevanatali.Thread;

import by.touchsoft.vasilyevanatali.Controller.SocketController;
import by.touchsoft.vasilyevanatali.Model.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;


/**
 * @author Natali
 * Thread that listen port and add users into need collection. Check first message from user, send server message to user
 */
public class SocketConnectionThread implements Runnable {

    /**
     * LOGGER variable to log connection information.
     */
    private static final Logger LOGGER = LogManager.getLogger(SocketConnectionThread.class);

    /**
     * Variable server - to create object of class Server to start program and use its methods
     */
    private final SocketController server;

    /**
     * Variable serverSocket - using for receive and send message to user through socket, what accept be server socket
     */
    private final ServerSocket serverSocket;

    /**
     * Constructor with parameters
     *
     * @param serverSocket - accept input request to open socket
     * @param server       - contain some method to connect and disconnect from server
     */
    public SocketConnectionThread(ServerSocket serverSocket, SocketController server) {
        this.server = server;
        this.serverSocket = serverSocket;
    }


    /**
     * Method that starts thread. Create input socket for each user, check is user agent or client, add users to need collections and run thread, what handles
     * message information coming from users and sends to clients or agents
     */
    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void run() {
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                User user = new User(socket);
                new Thread(new ConversationHandlerThread(user)).start();

            } catch (IOException e) {
                if (socket != null) {
                    LOGGER.error("Socket " + socket.getInetAddress() + " has been disconnected with exception " + e.getMessage());
                    disconnectSocket(socket);
                }
            }
        }
    }

    /**
     * Method that disconnect users from server if was an exception
     *
     * @param socket - user's socket to send or receive information from user
     */
    private synchronized void disconnectSocket(Socket socket) {
        try {
            if (socket != null) {
                socket.close();
            }
        } catch (IOException | NullPointerException ex) {
            LOGGER.error("Problem with disconnect socket " + ex.getMessage());
        }
    }
}
