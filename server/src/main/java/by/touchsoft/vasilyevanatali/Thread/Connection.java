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
                    sendString("You are connected. Please write the message.");
                    new ConnectOponnentThread(server, user).start();
                } else {
                    sendString("You are connected. Now one of client type to you the message");
                }

                System.out.println("you are here");
                System.out.println(user.getName());

                new Thread(new ConversationHandler(user, server)).start();
                //  }

            } catch (IOException e) {
                disconnect();
            }
        }

    }

    public synchronized void sendString(String value) {
        try {
            out.write(value + "\r\n");
            out.flush();

        } catch (IOException e) {

            //   disconnect();
        }
    }

    public synchronized void disconnect() {
        try {
            serverSocket.close();
        } catch (IOException e) {
        }
    }

//    @Override
//    public String toString() {
//        return "Connection" + socket.getInetAddress() + ": " + socket.getPort();
//    }

    private boolean checkFirstMessage(String message) {
        Matcher matcher = Pattern.compile("/reg (client|agent) [A-z]+").matcher(message);
        String userMessage = null;
        if (matcher.find()) {
            userMessage = matcher.group(0);
        }

        return message.equals(userMessage);
    }

//    public void findOpponent(User user) {
//        User opponent = null;
//        while (!user.isUserExit() && user.getOpponent() == null) {
//            if (user.getRole().equals("client")) {
//                try {
//                    opponent = server.getAgents().take();
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            } else if (user.getRole().equals("agent")) {
//                continue;
//            }
//        }
//        if (opponent != null) {
//            user.setOpponent(opponent);
//            opponent.setOpponent(user);
//            server.sendMessageToOpponent(user, "Client is connected");
//            server.sendMessageToOpponent(opponent, "Agent is connected");
//            opponent.setInConversation(true);
//            user.setInConversation(true);
//
//        }
//    }
}
