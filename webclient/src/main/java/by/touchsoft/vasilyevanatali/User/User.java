package by.touchsoft.vasilyevanatali.User;


import by.touchsoft.vasilyevanatali.Action.UserIdGenerator;
import by.touchsoft.vasilyevanatali.Message.ChatMessage;
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
    private String name;

    /**
     * User role - agent or client
     */
    private UserType role;

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
    private User opponent = null;

    private boolean isRestClient = false;

    /**
     * Contain messages from client while client hasn't opponent
     */
    private List<ChatMessage> messages = new LinkedList<>();

    private List<ChatMessage> messagesOfRestClient = new LinkedList<>();

    public User(){
        this.socket=null;
    }

    /**
     * Constructor with parameters. Open writer and reader from socket
     * @param socket - socket for open reader and writer stream
     * @param name - name of user
     * @param role - user's role (agent or client)
     */

    public User(Socket socket, String name, UserType role) {
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
     * @param socket - socket for open reader and writer stream
    */
    public User(Socket socket){
        this.socket = socket;
        this.userId = UserIdGenerator.createID();
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
     * @return userId
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     *
     * @param userId - set user id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
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

    public UserType getRole() {
        return role;
    }


    /**
     *
     * @param name set user name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @param role set user role
     */
    public void setRole(UserType role) {
        this.role = role;
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
     * If client in clients collection return true
     * @return true or false
     */

    public boolean isInClientCollection() {
        return isInClientCollection;
    }

    /**
     *
     * @param inClientCollection - set client status
     */

        public void setInClientCollection(boolean inClientCollection) {
        isInClientCollection = inClientCollection;
    }

    /**
     * return list with messages what has been received from client, when he hadn't opponent
     * @return List of messages
     */
    public List<ChatMessage> getMessages() {
        return messages;
    }

    public boolean isRestClient() {
        return isRestClient;
    }

    public void setRestClient(boolean restClient) {
        isRestClient = restClient;
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
        return
                isInConversation == user.isInConversation &&
                isUserExit == user.isUserExit &&
                isInClientCollection == user.isInClientCollection &&
                Objects.equals(socket, user.socket) &&
                Objects.equals(name, user.name) &&
                role == user.role &&
                Objects.equals(reader, user.reader) &&
                Objects.equals(writer, user.writer) &&
                Objects.equals(opponent, user.opponent) &&
                Objects.equals(messages, user.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(socket, name, role, reader, writer,  isInConversation, isUserExit, isInClientCollection, opponent, messages);
    }

    @Override
    public String toString() {
        return "User{" +
                "socket=" + socket +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", isInConversation=" + isInConversation +
                ", isUserExit=" + isUserExit +
                ", isInClientCollection=" + isInClientCollection +
                ", opponent=" + opponent +
                ", messages=" + messages +
                '}';
    }
}
