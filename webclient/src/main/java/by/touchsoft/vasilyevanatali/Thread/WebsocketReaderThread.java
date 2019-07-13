package by.touchsoft.vasilyevanatali.Thread;


import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Service.MessageServiceImpl;
import by.touchsoft.vasilyevanatali.WebClient.ChatEndPoint;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.time.LocalDateTime;

/**
 * @author Natali
 * Reading from server and sending to server  thread
 */
public class WebsocketReaderThread extends Thread {

    /**
     * LOGGER variable to log thread information
     */
    private static final Logger LOGGER = LogManager.getLogger(WebsocketReaderThread.class);

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
    public WebsocketReaderThread(ChatEndPoint chatEndPoint) {
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
        ChatMessage chatMessage;
        while (!socket.isClosed()) {
            try {
                message = reader.readLine();
                if (message != null) {
                    if (message.equals("/exit")) {
                        disconnectSocket();
                        break;
                    }
                    chatMessage = MessageServiceImpl.INSTANCE.parseFromJson(message);
                    String name = chatMessage.getSenderName();
                    String time = chatMessage.getTime().toString();
                    String text = chatMessage.getText();
                    chatEndPoint.sendMessageToWebPage(name + " (" + time + ") " + text);
                }
            } catch (SocketException exp) {
                LOGGER.debug("Problem with read from server", exp);

                chatEndPoint.sendMessageToWebPage("Server" + " (" + LocalDateTime.now().toString() + " )" + "Problem with server");
                disconnectSocket();
            } catch (IOException e) {
                chatEndPoint.sendMessageToWebPage("Server" + " (" + LocalDateTime.now().toString() + " )" + "Problem with server");
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

    public void sendMessageToServer(ChatMessage message) {
        try {
            String messageToSend = MessageServiceImpl.INSTANCE.convertToJson(message);
            writer.write(messageToSend);
            writer.newLine();
            writer.flush();

        } catch (IOException e) {
            LOGGER.debug("Sending message error (socket)", e);
            System.exit(0);
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
