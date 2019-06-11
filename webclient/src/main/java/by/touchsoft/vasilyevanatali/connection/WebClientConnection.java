package by.touchsoft.vasilyevanatali.connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.xml.ws.http.HTTPException;
import java.io.*;
import java.net.Socket;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WebClientConnection {
    private static final Logger LOGGER = LogManager.getLogger(WebClientConnection.class);
    private static final int PORT = 8189;
    private static final String HOST = "localhost";

    private Socket socket;
    private BufferedWriter socketWriter;
    private BufferedReader socketReader;


    public void connectToServer() {
        try {
            socket = new Socket(HOST, PORT);
            socketReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            socketWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            LOGGER.debug("Problem with open socket and get writer and reader " + e.getMessage());
            System.exit(0);

        }
    }

    public void disconnectFromServer() {
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

    public void sendMessageToServer(String message) {

        try {
            if (message != null) {
                sendMessage(message);

                if (message.equals("/exit")) {
                    System.out.println("Have a nice day");
                    disconnectFromServer();
                }
                if (message.equals("/leave")) {
                    System.out.println("We wait when you will begin to type again");
                }
            }
        } catch (HTTPException e) {
            LOGGER.debug("Problem with output process to socket", e.getMessage());
            System.exit(0);
        }
    }

    public String receiveMessage() {
        String message = null;
        while (!socket.isClosed()) {
            try {
                message = socketReader.readLine();

            } catch (IOException e) {
                LOGGER.debug("Problem with input process from socket" + e.getMessage());
                System.exit(0);
                message = null;
            }
        }
        return message;
    }

    public void sendMessage(String message) {

        try {
            socketWriter.write(message + "\r\n");
            socketWriter.flush();
        } catch (IOException e) {
            LOGGER.debug("Problem with output process to socket", e.getMessage());
            System.exit(0);
        }
    }

    public boolean checkFirstMessage(String message) {
        Matcher matcher = Pattern.compile("/reg (client|agent) [A-z]+").matcher(message);
        String userMessage = null;
        if (matcher.find()) {
            userMessage = matcher.group(0);
        }

        return message.equals(userMessage);
    }
}
