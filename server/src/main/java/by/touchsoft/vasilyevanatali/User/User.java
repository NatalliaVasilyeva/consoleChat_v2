package by.touchsoft.vasilyevanatali.User;



import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class User {
    private static final Logger LOGGER = LogManager.getLogger(User.class);
    private BufferedReader reader;
    private BufferedWriter writer;
    private Socket socket;
    private String name;
    private String role;
    private boolean isOnline;
    private boolean isInConversation;
    private boolean isUserExit;
    private User opponent;
    private List<String> messages = new LinkedList<>();

    public User() {
    }

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


    public BufferedReader getReader() {
        return reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public void setReader(BufferedReader reader) {
        this.reader = reader;
    }

    public void setWriter(BufferedWriter writer) {
        this.writer = writer;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isOnline == user.isOnline &&
                isInConversation == user.isInConversation &&
                isUserExit == user.isUserExit &&
                Objects.equals(reader, user.reader) &&
                Objects.equals(writer, user.writer) &&
                Objects.equals(socket, user.socket) &&
                Objects.equals(name, user.name) &&
                Objects.equals(role, user.role) &&
                Objects.equals(opponent, user.opponent) &&
                Objects.equals(messages, user.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(reader, writer, socket, name, role, isOnline, isInConversation, isUserExit, opponent, messages);
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
