package by.touchsoft.vasilyevanatali.WebClient;

import by.touchsoft.vasilyevanatali.Message.ChatMessage;
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
@ServerEndpoint(value = "/chat")
public class ChatEndPoint {

    private String name;

    /**
     * LOGGER variable to log websocket information
     */
    private static final Logger LOGGER = LogManager.getLogger(ChatEndPoint.class);

    /**
     * status of connection to server
     */
    private boolean isOnConnection = false;

    /**
     * Object of connectionThread class
     */
    private WebsocketReaderThread websocketReaderThread;

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
            chatMessage=new ChatMessage(name, LocalDateTime.now(), message);
            websocketReaderThread.sendMessageToServer(chatMessage);
            if (message.equals("/exit")) {
                isOnConnection = false;
                websocketReaderThread.disconnectSocket();
                websocketReaderThread = null;
                LOGGER.info("Client exit chat");
            }
        } else {
              connectToServer(message);
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
    public void sendMessageToWebPage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            LOGGER.error("Problem with send message to page", e);
        }
    }

    /**
     * Send message to server, start the reading from server thread
     *
     * @param message - first message with information about name, role of user
     */
    private void connectToServer(String message) {
        websocketReaderThread = new WebsocketReaderThread(this);
        websocketReaderThread.start();
        isOnConnection = true;
        getName(message);
        ChatMessage chatMessage = new ChatMessage(name, LocalDateTime.now(), message);
        websocketReaderThread.sendMessageToServer(chatMessage);
        LOGGER.info("Connected to server");
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