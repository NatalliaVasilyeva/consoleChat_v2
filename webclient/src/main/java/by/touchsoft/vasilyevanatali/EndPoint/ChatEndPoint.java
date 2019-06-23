package by.touchsoft.vasilyevanatali.EndPoint;

import by.touchsoft.vasilyevanatali.Thread.ConnectionThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;

/**
 * @author Natali
 * Endpoint for webscoket
 */
@ServerEndpoint(value = "/chat")
public class ChatEndPoint {

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
    private ConnectionThread connectionThread;

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
        if (isOnConnection) {
            connectionThread.sendMessageToServer(message);
            if (message.equals("/exit")) {
                isOnConnection = false;
                connectionThread.disconnectSocket();
                connectionThread = null;
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
     * @param e - twrowable error
     */
    @OnError
    public void onError(Throwable e) {
        sendMessageToWebPage("error");
        connectionThread.interrupt();
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
        connectionThread = new ConnectionThread(this);
        connectionThread.start();
        isOnConnection = true;
        connectionThread.sendMessageToServer(message);
        LOGGER.info("Connected to server");
    }

    /**
     * @param session - ChatEndPint session
     */
    public void setSession(Session session) {
        this.session = session;
    }
}