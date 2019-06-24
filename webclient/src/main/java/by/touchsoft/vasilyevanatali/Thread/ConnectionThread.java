package by.touchsoft.vasilyevanatali.Thread;


import by.touchsoft.vasilyevanatali.EndPoint.ChatEndPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.*;
import java.net.Socket;
import java.net.SocketException;

/**
 * @author Natali
 * Reading from server and sending to server  thread
 */
public class ConnectionThread extends Thread {

    /**
     * LOGGER variable to log thread information
     */
    private static final Logger LOGGER = LogManager.getLogger(ConnectionThread.class);

    /**
     * PORT and HOST variables are constants keeping appropriate information about connection.
     */
    private final String HOST = "localhost";
    private final int PORT = 8189;

    /**
     * Stream for receiver messages
     */
    private BufferedReader reader;

    /**
     * Stream sending messages
     */
    private BufferedWriter writer;

    /**
     * WebSocket endpoint
     */
    private ChatEndPoint chatEndPoint;

    /**
     * socket for open reader and writer stream
     */
    private Socket socket;


    /**
     * Constructor with parameters, that connect to server, open socket
     *
     * @param chatEndPoint - object of ChatEndPoint class
     */
    public ConnectionThread(ChatEndPoint chatEndPoint) {
        this.chatEndPoint = chatEndPoint;
        try {
            this.socket = new Socket(HOST, PORT);
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            LOGGER.info("Problem with connect to server", e);
        }
    }

    /**
     * Method to start thread, what receives messages from server
     */
    @Override
    public void run() {
        String message;
        while (!socket.isClosed()) {
            try {
                message = reader.readLine();
                if (message != null) {
                    if (message.equals("/exit")) {
                        disconnectSocket();
                        break;
                    }
                    chatEndPoint.sendMessageToWebPage(message);
                }
            } catch (SocketException exp) {
                LOGGER.debug("Problem with read from server", exp);
                chatEndPoint.sendMessageToWebPage("Problem with server");
                disconnectSocket();
            } catch (IOException e) {
                chatEndPoint.sendMessageToWebPage("Problem with server");
                this.interrupt();
                LOGGER.debug("Problem with read from server", e);
                disconnectSocket();
                break;
            } catch (NullPointerException ex) {
                LOGGER.debug("Problem with read from server", ex);
                disconnectSocket();
            }

        }
    }

    /**
     * Method to send message to opponent
     *
     * @param message = message from web user
     */
    public void sendMessageToServer(String message) {
        try {
            writer.write(message + "\r\n");
            writer.flush();
        } catch (IOException e) {
            chatEndPoint.sendMessageToWebPage("Problem with server");
            disconnectSocket();
            LOGGER.debug("Problem with write to server", e);
        }
    }

    /**
     * Method to close socket, when user stop to get chat
     */
    public void disconnectSocket() {
        try {
            if (writer != null) {
                writer.close();
            }
            if (reader != null) {
                reader.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            LOGGER.info("Something unexpected has happened", e);
        }

    }

}
