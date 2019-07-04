package by.touchsoft.vasilyevanatali.clientAction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import by.touchsoft.vasilyevanatali.Message.ChatMessage;
import by.touchsoft.vasilyevanatali.Service.IMessageService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Natali
 * Contain method, what help to connect to server, disconnect from server, send and receive message from server
 */
public class ClientToServerCommunicator {
    /**
     * LOGGER variable to log client information.
     */
    private static final Logger LOGGER = LogManager.getLogger(ClientToServerCommunicator.class);

    /**
     * PORT and HOST variables are constants keeping appropriate information about connection.
     */
    private static final int PORT = 8189;
    private static final String HOST = "localhost";

    /**
     * socket for open reader and writer stream
     */
    private Socket socket;

    /**
     * Stream sending messages
     */
    private BufferedWriter socketWriter;

    /**
     * Stream for receiver messages
     */
    private BufferedReader socketReader;

    /**
     * open stream to read and write messages
     */
    public void connectToServer() {
        try {
            socket = new Socket(HOST, PORT);
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            LOGGER.debug("Problem with open socket and get writer and reader " + e.getMessage());
        }
    }

    /**
     * Method destroy socket, when user disconnect from server
     */
    public void destroy() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
            if (socketReader != null) {
                socketReader.close();
            }
            if (socketWriter != null) {
                socketWriter.close();
            }
        } catch (IOException e) {
            System.exit(0);
            LOGGER.debug("Problem with disconnection" + e.getMessage());
        }
    }

    /**
     * Receive message from another user
     *
     * @return Message from opponent. Message receive from socket
     */
    public String receiveMessage() {
        while (!socket.isClosed()) {
            try {
                return socketReader.readLine();
            } catch (IOException e) {
                LOGGER.error("Problem with input process from socket " + e.getMessage());
                destroy();
                System.exit(0);
            }
        }
        return null;
    }

    /**
     * @param message - send message to server though the socket
     */
    public void sendMessage(IMessageService messageService, ChatMessage message) {
        try {
            String messageToSend = messageService.convertToJson(message);
            socketWriter.write(messageToSend);
            socketWriter.newLine();
            socketWriter.flush();

        } catch (IOException e) {
            LOGGER.debug("Sending message error (socket)", e);
            System.exit(0);
        }
    }
}
