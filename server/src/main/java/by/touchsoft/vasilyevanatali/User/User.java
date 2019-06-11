package by.touchsoft.vasilyevanatali.User;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;

public class User {
    private static final Logger LOGGER = LogManager.getLogger(User.class);
    private BufferedReader reader;
    private BufferedWriter writer;
    private HttpServletRequest req;
    private HttpServletResponse resp;
    private Socket socket;
    private String name;
    private String role;
    private boolean isOnline;
    private boolean isInConversation;
    private boolean isUserExit;
    private User opponent;
    private List<String> messages = new LinkedList<>();


    public User(Socket socket, String name, String role) {

        this.socket = socket;
        this.name = name;
        this.role = role;
        this.isUserExit = false;
        this.isOnline = true;
        this.isInConversation = false;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Problem with read or write to socket" + e.getMessage());
        }
    }

    public User(String name, String role, HttpServletRequest req, HttpServletResponse resp) {
        this.name = name;
        this.role = role;
        this.req = req;
        this.resp = resp;
    }

    public BufferedReader getReader() {
        return reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isInConversation() {
        return isInConversation;
    }

    public void setInConversation(boolean inConversation) {
        isInConversation = inConversation;
    }

    public User getOpponent() {
        return opponent;
    }

    public void setOpponent(User opponent) {
        this.opponent = opponent;
    }

    public boolean isUserExit() {
        return isUserExit;
    }

    public void setUserExit(boolean userExit) {
        isUserExit = userExit;
    }

    public List<String> getMessages() {
        return messages;
    }


    public void disconnectUser() {
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (!socket.isClosed()) {
                socket.close();
            }

        } catch (IOException e) {
            LOGGER.error("Problem with close user socket " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", role='" + role + '\'' +
                ", opponent=" + opponent +
                '}';
    }
}
