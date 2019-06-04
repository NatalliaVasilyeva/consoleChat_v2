package by.touchsoft.vasilyevanatali.User;


import by.touchsoft.vasilyevanatali.Server;

import java.io.*;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class User {
    BufferedReader reader;
    BufferedWriter writer;
    Socket socket;
    private String name;
    private String role;
    private boolean isOnline;
    private boolean isInConversation;
    private boolean isUserExit;
    private Server server;
    private User opponent;
    List<String> messages = new LinkedList<>();


    public User(Socket socket, String name, String role, Server server) {
        this.socket = socket;
        this.name = name;
        this.role = role;
        this.server = server;
        this.isUserExit = false;
        this.isOnline = true;
        this.isInConversation = false;
        try {
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF-8"));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF-8"));
        } catch (IOException e) {
        }
    }

    public BufferedReader getReader() {
        return reader;
    }

    public BufferedWriter getWriter() {
        return writer;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getName() {
        return name;
    }

    public String getRole() {
        return role;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public boolean isInConversation() {
        return isInConversation;
    }

    public void setInConversation(boolean inConversation) {
        isInConversation = inConversation;
    }

    public User getOpponent() {
        return opponent;
    }

    public void setOpponent(User opponent) {
        this.opponent = opponent;
    }

    public boolean isUserExit() {
        return isUserExit;
    }

    public void setUserExit(boolean userExit) {
        isUserExit = userExit;
    }

    public List<String> getMessages() {
        return messages;
    }

    public void addMessages(String message) {
        messages.add(message);
    }

    public void disconnectUser() {
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
            e.printStackTrace();
        }
    }

    public void exitUser(User user) {

        if (user.getRole().equals("client")) {
            server.sendMessageToOpponent(user, "Client with name " + user.getName() + " exit chat. We will find you a new opponent");
            User agent = user.getOpponent();
            server.addAgent(agent);
            agent.setOpponent(null);
            agent.setInConversation(false);
            user.setUserExit(true);
            server.removeClient(user);


        }
        if (user.getRole().equals("agent")) {
            User client = user.getOpponent();
            server.sendMessageToOpponent(user, "Agent with name " + user.getName() + " exit chat. We will find you a new opponent");
            server.addUser(client);
            client.setOpponent(null);
            client.setInConversation(false);
            user.setUserExit(true);
            server.removeAgent(user);

        }
    }

    public void disconnectFromAgent(User user) {
        server.sendMessageToOpponent(user, "Client with name " + user.getName() + " leave chat. We will find you a new opponent");
        User agent = user.getOpponent();
        server.addAgent(agent);
        agent.setOpponent(null);
        agent.setInConversation(false);
        user.setOpponent(null);
        user.setInConversation(false);
        user.setOnline(false);
        user.addMessages("/leave");

    }

}
