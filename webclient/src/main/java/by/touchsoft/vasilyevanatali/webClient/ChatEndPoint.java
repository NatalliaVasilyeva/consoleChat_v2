package by.touchsoft.vasilyevanatali.webClient;

import by.touchsoft.vasilyevanatali.Command.CommandFactory;
import by.touchsoft.vasilyevanatali.Enum.UserType;
import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Service.UserServiceSingleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;


/**
 * @author Natali
 * Endpoint for webscoket
 */

@ServerEndpoint(value = "/web/chat",
        encoders = MessageEncoder.class
)
public class ChatEndPoint {

    private String name;

    private  User user;

    /**
     * LOGGER variable to log websocket information
     */
    private static final Logger LOGGER = LogManager.getLogger(ChatEndPoint.class);

    /**
     * status of connection to server
     */
    private boolean isOnConnection = false;

    /**
     * Object of session class
     */
    private Session session;

    /**
     * Open chatEndPoint with websocket
     *
     * @param session - Session that has created when ChatEndPoint opens
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
    }

    /**
     * If this message equals "/exit" user disconnect from server, websocket wil be close
     *
     * @param message - message what send to opponent or server
     */
    @OnMessage
    public void onMessage(String message) {
        ChatMessage chatMessage;
        if (isOnConnection) {
            chatMessage = new ChatMessage(name, LocalDateTime.now(), message);
            CommandFactory commandFactory = new CommandFactory(user);
            commandFactory.startCommand(chatMessage);
            if (message.equals("/exit")) {
                isOnConnection = false;
                try {
                    session.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                LOGGER.info("Client exit chat");
            }
        } else {
            registerWebUser(message);
        }
    }

    /**
     * Close chatEndPoint
     */
    @OnClose
    public void onClose() {
        this.session = null;
    }

    /**
     * handle and log error
     *
     * @param e - throwable error
     */
    @OnError
    public void onError(Throwable e) {
        sendMessageToWebPage("error");
        LOGGER.error("error", e);
    }


    /**
     * Send message to web page
     *
     * @param message = message what send to web page from server or opponent
     */
    private void sendMessageToWebPage(String message) {
        ChatMessage chatMessage = new ChatMessage("Server", LocalDateTime.now(), message);
        try {
            session.getBasicRemote().sendObject(chatMessage);
        } catch (IOException | EncodeException e) {
            LOGGER.error("Problem with send message to page", e);
        }
    }

    /**
     * Send message to server, start the reading from server thread
     *
     * @param message - first message with information about name, role of user
     */
    private void registerWebUser(String message) {
        if (message != null) {
            if (user == null || user.isUserExit()) {
                isOnConnection=true;
                getName(message);
                ChatMessage chatMessage = new ChatMessage(name, LocalDateTime.now(), message);
                user = UserServiceSingleton.INSTANCE.registerUser(chatMessage);
                user.setSession(session);
                user.setType(UserType.WEB);
                UserServiceSingleton.INSTANCE.addUserToCollections(user);
                LOGGER.info("Connected to server");
            }
        }
    }
    /**
     * @param session - ChatEndPint session
     */
    public void setSession(Session session) {
        this.session = session;
    }

    private void getName(String message) {
        String[] splittedFirstMessage = message.split(" ");
        name = splittedFirstMessage[2];
    }
}