package by.touchsoft.vasilyevanatali.Thread;


import by.touchsoft.vasilyevanatali.ConversationHandler;
import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.User.User;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Connection implements Runnable {

    private ServerSocket serverSocket;
    private BufferedReader in;
    private BufferedWriter out;
    private final Server server;

    public Connection(ServerSocket serverSocket, Server server) {
        this.server = server;
        this.serverSocket = serverSocket;
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                in = new BufferedReader(new InputStreamReader(socket.getInputStream(), Charset.forName("UTF-8")));
                out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), Charset.forName("UTF-8")));
                String regString = in.readLine();
                while (!checkFirstMessage(regString)) {
                    out.write("Please, check you information \n");
                    regString = in.readLine();
                }
                String[] splittedFirstMessage = regString.split(" ");
                String role = splittedFirstMessage[1];
                String name = splittedFirstMessage[2];
                User user = new User(socket, name, role, server);
                server.addUser(user);
                if (role.equals("client")) {
                    sendString("You are connected. Please wait when we find the agent to you");
                    new ConnectOponnentThread(server, user).start();
                } else {
                    sendString("You are connected. Now one of client type to you the message");
                }


                new Thread(new ConversationHandler(user, server)).start();

            } catch (IOException e) {
                server.disconnectServer(serverSocket);
            }
        }

    }

    public synchronized void sendString(String value) {
        try {
            out.write(value + "\r\n");
            out.flush();

        } catch (IOException e) {
            server.disconnectServer(serverSocket);
        }
    }



    private boolean checkFirstMessage(String message) {
        Matcher matcher = Pattern.compile("/reg (client|agent) [A-z]+").matcher(message);
        String userMessage = null;
        if (matcher.find()) {
            userMessage = matcher.group(0);
        }

        return message.equals(userMessage);
    }

}
