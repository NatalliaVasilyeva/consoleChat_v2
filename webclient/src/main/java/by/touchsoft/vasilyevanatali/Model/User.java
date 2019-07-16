package by.touchsoft.vasilyevanatali.Model;


import by.touchsoft.vasilyevanatali.Enum.UserRole;
import by.touchsoft.vasilyevanatali.Enum.UserType;
import by.touchsoft.vasilyevanatali.util.UserIdGenerator;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.Session;
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

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class User {
    /**
     * LOGGER variable to log user information.
     */
    private static final Logger LOGGER = LogManager.getLogger(User.class);

    /**
     * socket for open reader and writer stream
     */
    private  Socket socket;


    private  Session session;

    /**
     * User name
     */

    private String name;

    /**
     * User role - agent or client
     */

    private UserRole role;

    /**
     * Stream for receiver messages
     */
    private BufferedReader reader;

    /**
     * Stream sending messages
     */
    private BufferedWriter writer;

    /**
     * User id
     */

    private Integer userId;

    /**
     * show is user in conversation. Variable become true when user find opponent
     */
    private boolean isInConversation = false;

    /**
     * show is user exit or now. Become true when user send message - "/exit"
     */
    private boolean isUserExit = true;


    private boolean isInClientCollection = false;
    /**
     * Show is user has opponent. Not null when user find opponent
     */
    @JsonIgnore
    private User opponent = null;

    /**
     * Show is client restClient
     */
    private boolean isRestClient = false;


    /**
     * Type of user - console, web or rest
     */
    private UserType type;

    /**
     * Contain messages from client while client hasn't opponent
     */

    private List<ChatMessage> messages = new LinkedList<>();

    /**
     * Contain messages for rest client
     */
    private List<ChatMessage> messagesOfRestClient = new LinkedList<>();


    /**
     * Constructor without parameters
     */
    public User() {

    }


    /**
     * Constructor with parameters
     * @param name - name of user
     * @param role - user's role
     */
    public User (String name, UserRole role) {
        this.name=name;
        this.role=role;
        this.userId = UserIdGenerator.createID();
    }

    /**
     * @return BufferedReader
     */
    public BufferedReader getReader() {
        return reader;
    }

    /**
     * @return BufferedWriter
     */
    public BufferedWriter getWriter() {
        return writer;
    }


    /**
     * @return userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * @param userId - set user id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * @return Socket
     */
    public Socket getSocket() {
        return socket;
    }


    /**
     * Set socket and reader and writer for it
     * @param socket - socket of console user
     */
    public void setSocket(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Problem with read or write to socket" + e.getMessage());
        }
    }

    /**
     * Set session for web client
     * @param session - session of web client
     */
    public void setSession(Session session) {
        this.session = session;
    }

    /**
     * @return user name
     */
    public String getName() {
        return name;
    }

    /**
     * @return user role
     */

    public UserRole getRole() {
        return role;
    }


    /**
     * @param name set user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param role set user role
     */
    public void setRole(UserRole role) {
        this.role = role;
    }


    /**
     * If user has opponent return true
     *
     * @return true or false
     */
    public boolean isInConversation() {
        return isInConversation;
    }

    /**
     * @param inConversation - set true when user find opponent
     */
    public void setInConversation(boolean inConversation) {
        isInConversation = inConversation;
    }

    /**
     * @return User - agent or client
     */
    public User getOpponent() {
        return opponent;
    }

    /**
     * Use when find opponent to user
     *
     * @param opponent - set User (agent or client)
     */

    public void setOpponent(User opponent) {
        this.opponent = opponent;
    }

    /**
     * If user send "/exit" return false
     *
     * @return true or false
     */
    public boolean isUserExit() {
        return isUserExit;
    }

    /**
     * If user send "/exit" set true
     *
     * @param userExit - user exit
     */
    public void setUserExit(boolean userExit) {
        isUserExit = userExit;
    }

    /**
     * If client in clients collection return true
     *
     * @return true or false
     */

    public boolean isInClientCollection() {
        return isInClientCollection;
    }

    /**
     * @param inClientCollection - set client status
     */

    public void setInClientCollection(boolean inClientCollection) {
        isInClientCollection = inClientCollection;
    }

    /**
     * return list with messages what has been received from client, when he hadn't opponent
     *
     * @return List of messages
     */
    public List<ChatMessage> getMessages() {
        return messages;
    }


    /**
     * check client for rest type
     * @return true of false
     */
    public boolean isRestClient() {
        return isRestClient;
    }

    /**
     * return web, console or rest type of client
     * @return user type
     */
    public UserType getType() {
        return type;
    }

    /**
     *  Set user type
     * @param type - type of client
     */
    public void setType(UserType type) {
        this.type = type;
    }

    /**
     * Set user's psrsmeter for rest client
     * @param restClient - true or false
     */
    public void setRestClient(boolean restClient) {
        isRestClient = restClient;
    }

    /**
     *  Return session of web client
     * @return session
     */
    public Session getSession() {
        return session;
    }

    /**
     *
     * @param messages - list of chat messages
     */
    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

      /**
     * Method close socket when user send "/exit" and go out from program
     */
    public void disconnectUserByServer() {
        try {
            if (socket!=null) {
                if (reader != null) {
                    reader.close();
                }
                if (writer != null) {
                    writer.close();
                }
                if (!socket.isClosed()) {
                    socket.close();
                }
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
        return isInConversation == user.isInConversation &&
                isUserExit == user.isUserExit &&
                isInClientCollection == user.isInClientCollection &&
                isRestClient == user.isRestClient &&
                Objects.equals(socket, user.socket) &&
                Objects.equals(name, user.name) &&
                role == user.role &&
                Objects.equals(reader, user.reader) &&
                Objects.equals(writer, user.writer) &&
                Objects.equals(userId, user.userId) &&
                Objects.equals(opponent, user.opponent) &&
                type == user.type &&
                Objects.equals(messages, user.messages) &&
                Objects.equals(messagesOfRestClient, user.messagesOfRestClient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, name, role, reader, writer, userId, isInConversation, isUserExit, isInClientCollection, opponent, isRestClient, type, messages, messagesOfRestClient);
    }

    @Override
    public String toString() {
        String sb = "User{" + "name='" + name + '\'' +
                ", role=" + role +
                ", userId=" + userId +
                ", isInConversation=" + isInConversation +
                ", isUserExit=" + isUserExit +
                ", isInClientCollection=" + isInClientCollection +
                ", isRestClient=" + isRestClient +
                ", type=" + type +
                '}';
        return sb;
    }
}
