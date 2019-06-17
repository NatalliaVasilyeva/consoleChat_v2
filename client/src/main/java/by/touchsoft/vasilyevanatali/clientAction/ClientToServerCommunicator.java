package by.touchsoft.vasilyevanatali.clientAction;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class ClientToServerCommunicator {
    private static final Logger LOGGER = LogManager.getLogger(ClientToServerCommunicator.class);

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
            LOGGER.debug("Problem with open socket and get writer and reader "+ e.getMessage());

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


    public String receiveMessage() {

        try {
            return socketReader.readLine();
        } catch (IOException e) {
            LOGGER.debug("Problem with input process from socket" +  e.getMessage());
            System.exit(0);
            return null;
        }
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
}
