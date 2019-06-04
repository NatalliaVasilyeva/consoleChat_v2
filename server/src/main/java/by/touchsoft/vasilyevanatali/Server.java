package by.touchsoft.vasilyevanatali;


import by.touchsoft.vasilyevanatali.Thread.Connection;
import by.touchsoft.vasilyevanatali.User.User;


import java.io.BufferedWriter;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Server {
    public static final int PORT = 8189;

    private List<User> clients = Collections.synchronizedList(new ArrayList<>());
    private BlockingQueue<User> agents = new ArrayBlockingQueue<>(100);
    private ServerSocket serverSocket;


    public Server() {
        System.out.println("Server is running ...");
        try {
            serverSocket = new ServerSocket(PORT);
            new Thread(new Connection(serverSocket, this)).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    public synchronized void addUser(User user) {
        if (user.getRole().equals("client")) {
            clients.add(user);
        } else {
            try {
                agents.put(user);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    public synchronized void addAgent(User user) {
        try {
            agents.put(user);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void sendMessageToOpponent(User user, String message) {
        try {
            BufferedWriter writer = user.getOpponent().getWriter();
            writer.write(message + "\r\n");
            writer.flush();
            System.out.println("end of method");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public synchronized void addClient(User user) {
        clients.add(user);
    }

    public synchronized void removeAgent(User user) {
        agents.remove(user);
    }

    public synchronized void removeClient(User user) {
        clients.remove(user);
    }


    public User getAgent() {
        try {
            return agents.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            return null;
        }
    }

    public synchronized void disconnectServer(ServerSocket serverSocket) {
        try {

            if (serverSocket != null) {
                serverSocket.close();
            }
            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

}

