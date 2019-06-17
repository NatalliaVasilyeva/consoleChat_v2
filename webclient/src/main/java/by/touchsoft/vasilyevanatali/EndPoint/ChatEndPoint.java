package by.touchsoft.vasilyevanatali.EndPoint;

import by.touchsoft.vasilyevanatali.Thread.InputMessageThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

@ServerEndpoint(value = "/chat")
public class ChatEndPoint {

    private static final Logger LOGGER = LogManager.getLogger(ChatEndPoint.class);

    private Session session;
    private Socket socket;
    private BufferedWriter writer;
    private InputMessageThread inputMessageThread;
   private Thread readThread;
    public boolean isExit = false;


    @OnOpen
    public void onOpen(Session session) throws IOException {
        this.session = session;
        this.socket = new Socket("localhost", 8189);
        this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        inputMessageThread = new InputMessageThread(session, socket, isExit);
        readThread = new Thread(inputMessageThread);
        readThread.start();

    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException {
        sendMessageToServer(message);
        if (message.equals("/exit")) {
            isExit = true;
            disconnectFromServer();
            readThread.interrupt();
            session.close();
        }
    }

    @OnClose
    public void onClose(Session session) throws IOException {
        session.close();
        readThread.interrupt();


    }

    @OnError
    public void onError(Session session, Throwable throwable) {

        sendMessageToWebPage(session, "error");
        throwable.printStackTrace();
    }


    private void sendMessageToWebPage(Session session, String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendMessageToServer(String message) {
        try {
            writer.write(message + "\r\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void disconnectFromServer() {
        try {
            if (!socket.isClosed()) {
                socket.close();
            }
            if (writer != null) {
                writer.close();
            }
        } catch (IOException e) {
            System.exit(0);
            LOGGER.debug("Problem with disconnection" + e.getMessage());

        }

    }
}