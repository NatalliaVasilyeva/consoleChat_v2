package by.touchsoft.vasilyevanatali.Model;


import by.touchsoft.vasilyevanatali.Enum.UserRole;
import by.touchsoft.vasilyevanatali.Enum.UserType;
import by.touchsoft.vasilyevanatali.Util.UserIdGenerator;
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

    private boolean isRestClient = false;

    private UserType type;

    /**
     * Contain messages from client while client hasn't opponent
     */

    private List<ChatMessage> messages = new LinkedList<>();


    private List<ChatMessage> messagesOfRestClient = new LinkedList<>();

    public User() {
        this.userId = UserIdGenerator.createID();
        this.socket = null;
    }

    /**
     * Constructor with parameters. Open writer and reader from socket
     *
     * @param socket - socket for open reader and writer stream
     * @param name   - name of user
     * @param role   - user's role (agent or client)
     */

    public User(Socket socket, String name, UserRole role) {
        this.socket = socket;
        this.name = name;
        this.role = role;
        this.userId = UserIdGenerator.createID();
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Problem with read or write to socket" + e.getMessage());
        }
    }

    /**
     * Constructor with parameters. Open writer and reader from socket
     *
     * @param socket - socket for open reader and writer stream
     */
    public User(Socket socket) {
        this.socket = socket;
        this.userId = UserIdGenerator.createID();
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Problem with read or write to socket" + e.getMessage());
        }
    }

    public User(Session session) {
        this.session = session;
        this.userId = UserIdGenerator.createID();
    }

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

    public void setSocket(Socket socket) {
        this.socket = socket;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8));
        } catch (IOException e) {
            LOGGER.error("Problem with read or write to socket" + e.getMessage());
        }
    }

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


    public boolean isRestClient() {
        return isRestClient;
    }

    public UserType getType() {
        return type;
    }

    public void setType(UserType type) {
        this.type = type;
    }

    public void setRestClient(boolean restClient) {
        isRestClient = restClient;
    }

    public Session getSession() {
        return session;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public List<ChatMessage> getMessagesOfRestClient() {
        return messagesOfRestClient;
    }

    public void setMessagesOfRestClient(List<ChatMessage> messagesOfRestClient) {
        this.messagesOfRestClient = messagesOfRestClient;
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
        final StringBuffer sb = new StringBuffer("User{");
        sb.append("name='").append(name).append('\'');
        sb.append(", role=").append(role);
        sb.append(", userId=").append(userId);
        sb.append(", isInConversation=").append(isInConversation);
        sb.append(", isUserExit=").append(isUserExit);
        sb.append(", isInClientCollection=").append(isInClientCollection);
        sb.append(", opponent=").append(opponent);
        sb.append(", isRestClient=").append(isRestClient);
        sb.append(", type=").append(type);
        sb.append('}');
        return sb.toString();
    }
}
