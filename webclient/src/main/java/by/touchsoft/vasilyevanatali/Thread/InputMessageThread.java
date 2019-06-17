package by.touchsoft.vasilyevanatali.Thread;

import by.touchsoft.vasilyevanatali.EndPoint.ChatEndPoint;

import javax.websocket.Session;
import java.io.*;
import java.net.Socket;

public class InputMessageThread implements Runnable {

    private Session session;
    private Socket socket;
    private BufferedReader reader;
    private boolean isExit;
    ChatEndPoint chatEndPoint;


    public InputMessageThread(Session session, Socket socket, boolean isExit) {
        this.session = session;
        this.socket = socket;
        this.isExit = isExit;
        this.chatEndPoint = new ChatEndPoint();
        try {
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void run() {
        String message;
        while (!isExit) {
            try {
                if (reader.ready()) {
                    message = reader.readLine();
                    session.getBasicRemote().sendText(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
                try {
                    session.getBasicRemote().sendText("Some problem with server");
                    System.exit(0);
                } catch (IOException ex) {
                    ex.getMessage();
                }
            }
        }
    }

}
