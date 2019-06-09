package by.touchsoft.vasilyevanatali.User;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;


import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class UsersAction {
    private static final Logger LOGGER = LogManager.getLogger(UsersAction.class);
    private BlockingDeque<User> clients = new LinkedBlockingDeque<>();
    private BlockingQueue<User> agents = new ArrayBlockingQueue<>(100);

    public void addUser(User user) {
        if (user.getRole().equals("client")) {
            addClient(user);
        } else {
            addAgent(user);
        }
    }


    public void addAgent(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        try {
            agents.put(user);
        } catch (InterruptedException e) {
            LOGGER.error("Problem with add user to collection with agents " + e.getLocalizedMessage());
        }
    }

    public void addClient(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        try {
            clients.put(user);
        } catch (InterruptedException e) {
            LOGGER.error("Problem with add user to collection with clients " + e.getLocalizedMessage());
        }
    }


    public void removeAgent(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        agents.remove(user);
    }

    public void removeClient(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        clients.remove(user);
    }

    public void removeClientFromDequeForConversation() {
        clients.poll();
    }

    public User getAgent() {
        try {
            return agents.take();
        } catch (InterruptedException e) {
            LOGGER.error("Problem with take agent to collection  " + e.getLocalizedMessage());
            return null;
        }
    }

    public void sendMessageToOpponent(User user, String message) {
        try {
            BufferedWriter writer = user.getOpponent().getWriter();
            writer.write(message + "\r\n");
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("Problem with sending message to opponent  " + e.getLocalizedMessage());
        }
    }


    public void exitUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }

        if (user.getRole().equals("client")) {
            sendMessageToOpponent(user, "Client with name " + user.getName() + " exit chat. We will find you a new opponent");
            User agent = user.getOpponent();
            addAgent(agent);
            agent.setOpponent(null);
            agent.setInConversation(false);
            user.setUserExit(true);
            removeClient(user);
            LOGGER.info("User with name" + user.getName() + " with role "+ user.getRole() + " exit chat");

        }
        if (user.getRole().equals("agent")) {
            User client = user.getOpponent();
            sendMessageToOpponent(user, "Agent with name " + user.getName() + " exit chat. We will find you a new opponent");
            addUser(client);
            client.setOpponent(null);
            client.setInConversation(false);
            user.setUserExit(true);
            removeAgent(user);
            LOGGER.info("User with name" + user.getName() + " with role "+ user.getRole() + " exit program");
        }
    }

    public void disconnectFromAgent(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        sendMessageToOpponent(user, "Client with name " + user.getName() + " leave chat. We will find you a new opponent");
        User agent = user.getOpponent();
        addAgent(agent);
        agent.setOpponent(null);
        agent.setInConversation(false);
        user.setOpponent(null);
        user.setInConversation(false);
        user.setOnline(false);
        LOGGER.info("Client with name " + user.getName() + " leave chat. ");

    }

    public void connectToOpponent() {
        User opponent;

        for (User client : clients) {
            while (!client.isUserExit() && client.getOpponent() == null) {
                opponent = getAgent();
                removeClientFromDequeForConversation();

                if (opponent != null) {
                    client.setOpponent(opponent);
                    opponent.setOpponent(client);
                    sendMessageToOpponent(client, "Client is connected");
                    sendMessageToOpponent(opponent, "Agent is connected");
                    opponent.setInConversation(true);
                    client.setInConversation(true);
                    LOGGER.info("Client with name " + client.getName() + " find opponent with name " + opponent.getName());
                }
            }

        }
    }


}
