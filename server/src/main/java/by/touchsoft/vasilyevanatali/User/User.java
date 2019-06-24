package by.touchsoft.vasilyevanatali.User;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

/**
 * @author Natali
 * Entity class User
 */

public class User {
    /**
     * LOGGER variable to log user information.
     */
    private static final Logger LOGGER = LogManager.getLogger(User.class);

    /**
     * socket for open reader and writer stream
     */
    private final Socket socket;

    /**
     * User name
     */
    private final String name;

    /**
     * User role - agent or client
     */
    private final String role;

    /**
     * Stream for receiver messages
     */
    private BufferedReader reader;

    /**
     * Stream sending messages
     */
    private BufferedWriter writer;

    /**
     * show is user online or not. When server has first message from client this status become true
     */
    private boolean isOnline = true;

    /**
     * show is user in conversation. Variable become true when user find opponent
     */
    private boolean isInConversation = false;

    /**
     * show is user exit or now. Become true when user send message - "/exit"
     */
    private boolean isUserExit = false;

    /**
     * Show is user has opponent. Not null when user find opponent
     */
    private User opponent = null;

    /**
     * Contain messages from client while client hasn't opponent
     */
    private List<String> messages = new LinkedList<>();


    /**
     * Constructor with parameters. Open writer and reader from socket
     * @param socket - socket for open reader and writer stream
     * @param name - name of user
     * @param role - user's role (agent or client)
     */
    public User(Socket socket, String name, String role) {
        this.socket = socket;
        this.name = name;
        this.role = role;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Problem with read or write to socket" + e.getMessage());
        }
    }

    /**
     *
     * @return BufferedReader
     */
    public BufferedReader getReader() {
        return reader;
    }

    /**
     *
     * @return BufferedWriter
     */
    public BufferedWriter getWriter() {
        return writer;
    }

    /**
     *
      * @return Socket
     */
    public Socket getSocket() {
        return socket;
    }

    /**
     *
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @return user role
     */

    public String getRole() {
        return role;
    }

    /**
     * If user online return true
     * @return true or false
     */

    public boolean isOnline() {
        return isOnline;
    }

    /**
     *
     * @param online - set true, when user write to server
     */
    public void setOnline(boolean online) {
        isOnline = online;
    }

    /**
     * If user has opponent return true
     * @return true or false
     */
    public boolean isInConversation() {
        return isInConversation;
    }

    /**
     *
     * @param inConversation - set true when user find opponent
     */
    public void setInConversation(boolean inConversation) {
        isInConversation = inConversation;
    }

    /**
     *
     * @return User - agent or client
     */
    public User getOpponent() {
        return opponent;
    }

    /**
     * Use when find opponent to user
     * @param opponent - set User (agent or client)
     */

    public void setOpponent(User opponent) {
        this.opponent = opponent;
    }

    /**
     * If user send "/exit" return false
     * @return true or false
     */
    public boolean isUserExit() {
        return isUserExit;
    }

    /**
     * If user send "/exit" set true
     * @param userExit - user exit
     */
    public void setUserExit(boolean userExit) {
        isUserExit = userExit;
    }

    /**
     * return list with messages what has been received from client, when he hadn't opponent
     * @return List of messages
     */
    public List<String> getMessages() {
        return messages;
    }

    /**
     * Method close socket when user send "/exit" and go out from program
     */
    public void disconnectUserByServer() {
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
