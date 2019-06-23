package by.touchsoft.vasilyevanatali.Thread;

import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.User.User;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author Natali
 * Thread that listen port and add users into need collection. Check first message from user, send server message to user
 */
public class Connection implements Runnable {

    /**
     * LOGGER variable to log connection information.
     */
    private static final Logger LOGGER = LogManager.getLogger(Connection.class);

    /**
     * Variable server - to create object of class Server to start program and use its methods
     */
    private final Server server;

    /**
     * Variable serverSocket - using for receive and send message to user through socket, what accept be server socket
     */
    private final ServerSocket serverSocket;

    /**
     * Stream for receiver messages
     */
    private BufferedReader in;

    /**
     * Stream sending messages
     */
    private BufferedWriter out;

    /**
     * Constructor with parameters
     * @param serverSocket - accept input request to open socket
     * @param server - contain some method to connect and disconnect from server
     */
    public Connection(ServerSocket serverSocket, Server server) {
        this.server = server;
        this.serverSocket = serverSocket;
    }


    /**
     * Method that starts thread. Create input socket for each user, check is user agent or client, add users to need collections and run thread, what handles
     * message information coming from users and sends to clients or agents
     */
    @Override
    public void run() {
        while (true) {
            Socket socket = null;
            try {
                socket = serverSocket.accept();
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
                String regString = in.readLine();
                while (!checkFirstMessage(regString)) {
                    out.write("Please, check you information \n");
                    regString = in.readLine();
                }
                String[] splittedFirstMessage = regString.split(" ");
                String role = splittedFirstMessage[1];
                String name = splittedFirstMessage[2];
                User user = new User(socket, name, role);
                server.getUsersAction().addUser(user);
                LOGGER.info("User with name " + user.getName() + " and role " + user.getRole() + " come to chat");
                if (role.equals("client")) {
                    sendServerMessage("You are connected. Please wait when we find the agent to you");
                } else {
                    sendServerMessage("You are connected. Now client type to you the message");
                }
                new Thread(new ConversationHandler(user, server.getUsersAction())).start();
            } catch (IOException e) {
                if (socket != null) {
                    LOGGER.error("Socket " + socket.getInetAddress() + " has been disconnected with exception " + e.getMessage());
                }
                disconnectSocket(socket);
            }
        }
    }

    /**
     * Method what send server message from server to client. For example, "You are connected". Include data information.
     * @param value - message from server to user
     */
    private synchronized void sendServerMessage(String value) {
        String data;
        LocalDateTime time;
        try {
            time = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            data = time.format(formatter);
            out.write("Server " + "(" + data + ")" + " " + value + "\r\n");
            out.flush();
        } catch (IOException e) {
            LOGGER.error("Problem with send message from server to user " + e.getMessage());
        }
    }

    /**
     * Method help to check information about user. If information is bad - ask to repeat message
     * @param message - input first message from client
     * @return true or false. False - when message is wrong
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
     * Method that disconnect users from server if was an exception
     * @param socket - user's socket to send or receive information from user
     */
    private synchronized void disconnectSocket(Socket socket) {
        try {
            if (in != null) {
                in.close();
            }
            if (out != null) {
                out.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException ex) {
            LOGGER.error("Problem with disconnect socket " + ex.getMessage());
        }
    }
}
