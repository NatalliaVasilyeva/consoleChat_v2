package by.touchsoft.vasilyevanatali;

import by.touchsoft.vasilyevanatali.clientAction.ClientToServerCommunicator;
import by.touchsoft.vasilyevanatali.clientAction.ConsoleReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Natali
 * Contain methods, what help to connect to server, disconnect from server, send and receive message from server throught the threads
 */

public class Client {
    /**
     * LOGGER variable to log client information.
     */
    private static final Logger LOGGER = LogManager.getLogger(Client.class);

    /**
     * readerThread variable to read message from server while client is online
     */
    private final Thread readerThread;

    /**
     * Create object of clientToServerCommunicator for using it's methods to connect, disconnect from server, send and receive messages
     */
    private final ClientToServerCommunicator connector = new ClientToServerCommunicator();

    /**
     * Create object of consoleReader for using it's methods to read from console
     */
    private final ConsoleReader consoleReader = new ConsoleReader();

    /**
     * Variable what using to check information about client conditionals(is online or is exit)
     */
    private boolean isStopped = false;

    /**
     * Constructor without parameters
     * Using for register user, read and write messages to server while user is not exit
     *
     */
    public Client() {
        System.out.println("Hello. Please write #/reg client NAME# if you are the client or #/reg agent NAME# if you are the agent ");
        String message = consoleReader.readFromConsole();
        while (!checkFirstMessage(message)) {
            System.out.println("Please, check you information");
            message = consoleReader.readFromConsole();
        }
        connectClientToServer();
        connector.sendMessage(message);
        readerThread = new Thread(() -> {
            while (!Client.this.isStopped()) {
                showMessageFromServer();
            }
        });
        readerThread.start();
        while (!isStopped) {
            sendMessageFromClient();
        }
    }

    /**
     *
     * @param message - first message from client. Using for find out information about user and check right information for register
     * @return true or false. If wrong information - return false, if right - true
     */
    private boolean checkFirstMessage(String message) {
        Matcher matcher = Pattern.compile("/reg (client|agent) [A-z]+").matcher(message);
        String userMessage = null;
        if (matcher.find()) {
            userMessage = matcher.group(0);
        }
        return message.equals(userMessage);
    }

    /**
     * Method help connect to server
     */
    private void connectClientToServer() {
        connector.connectToServer();
    }

    /**
     * Method call when user write "/exit". All threads interrupt, close socket and exit from program
     */
    private void disconnectClientFromServer() {
        readerThread.interrupt();
        connector.destroy();
        consoleReader.destroy();
        setStopped();
        System.exit(0);
    }

    /**
     * Method send message from client to opponent or server. Read string from console. Check message and after that exit from program, leave program or
     * continue conversation
     */
    private void sendMessageFromClient() {
        String message = consoleReader.readFromConsole();
        if (message != null) {
            connector.sendMessage(message);
            if (message.equals("/exit")) {
                System.out.println("Have a nice day");
                disconnectClientFromServer();
                LOGGER.info("disconnect from server");
            }
            if (message.equals("/leave")) {
                System.out.println("We wait when you will begin to type again");
            }
        }
    }

    /**
     * Method receive string from opponent or server and show it on console
     */
    private void showMessageFromServer() {
        String message = connector.receiveMessage();
        if (message != null) {
            System.out.println(message);
        }
    }

    /**
     * Method set variable isStopped equals true when user input "/exit"
     */
    private void setStopped() {
        this.isStopped = true;
    }

    /**
     *
     * @return true or false. Use when need to check is user exit or not
     */
    private boolean isStopped() {
        return isStopped;
    }
}
