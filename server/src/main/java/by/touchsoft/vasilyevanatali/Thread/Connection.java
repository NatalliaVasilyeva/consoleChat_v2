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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connection implements Runnable {

    private static final Logger LOGGER = LogManager.getLogger(Connection.class);
    private ServerSocket serverSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private final Server server;


    public Connection(ServerSocket serverSocket, Server server) {
        this.server = server;
        this.serverSocket = serverSocket;
    }

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
                    sendString("You are connected. Please wait when we find the agent to you");

                } else {
                    sendString("You are connected. Now one of client type to you the message");
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

    private synchronized void sendString(String value) {
        try {
            out.write(value + "\r\n");
            out.flush();

        } catch (IOException e) {
            LOGGER.error("Problem with send message from server to user " + e.getMessage());
        }
    }


    private boolean checkFirstMessage(String message) {
        Matcher matcher = Pattern.compile("/reg (client|agent) [A-z]+").matcher(message);
        String userMessage = null;
        if (matcher.find()) {
            userMessage = matcher.group(0);
        }
        return message.equals(userMessage);
    }

    public void disconnectSocket(Socket socket) {
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
