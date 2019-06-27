package by.touchsoft.vasilyevanatali.User;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedWriter;
import java.io.IOException;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * @author Natali
 * Include method what using by clent or agent
 */
public class UsersAction {

    /**
     * LOGGER variable to log user actions
     */
    private static final Logger LOGGER = LogManager.getLogger(UsersAction.class);

    /**
     * Collections to save clients
     */
    private BlockingDeque<User> clients = new LinkedBlockingDeque<>();

    /**
     * Collections to save agents
     */
    private BlockingQueue<User> agents = new ArrayBlockingQueue<>(100);

    /**
     * return collections with agents
     *
     * @return BlockingQueue of agents
     */
    public BlockingQueue<User> getAgents() {
        return agents;
    }

    /**
     * Method to add user to need collections (agents or clients)
     *
     * @param user - new user in server, who want to get chat
     */
    public void addUser(User user) {
        if (user.getRole().equals(UserType.CLIENT)) {
            addClient(user);
        } else {
            addAgent(user);
        }
    }

    /**
     * Add agent into agents collections
     *
     * @param user - new user in server, who want to get chat
     */
    private void addAgent(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        try {
            agents.put(user);
        } catch (InterruptedException e) {
            LOGGER.error("Problem with adding user to agent collection " + e.getLocalizedMessage());
        }
    }

    /**
     * Add client into clients collections
     *
     * @param user- new user in server, who want to get chat
     */
    private void addClient(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        try {
            clients.put(user);
        } catch (InterruptedException e) {
            LOGGER.error("Problem with add user to collection with clients " + e.getLocalizedMessage());
        }
    }

    /**
     * Method that use for send message to opponent
     *
     * @param user    - client or agent who send the message
     * @param message - user's message to opponent
     */
    public void sendMessageToOpponent(User user, String message) {
        String data;
        LocalDateTime time;
        try {
            BufferedWriter writer = user.getOpponent().getWriter();
            time = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            data = time.format(formatter);
            writer.write(user.getName() + " " + "(" + data + ")" + " " + message + "\r\n");
            writer.flush();
        } catch (IOException e) {
            LOGGER.error("Problem with sending message to opponent  " + e.getLocalizedMessage());
        }
    }

    /**
     * Method that use for send message to user from server
     *
     * @param user    - client or agent to who the message will be send
     * @param value - server's message to user
     */
    public synchronized void sendServerMessage(String value, User user) {
        String data;
        LocalDateTime time;
        try {
            time = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            data = time.format(formatter);
            user.getWriter().write("Server " + "(" + data + ")" + " " + value + "\r\n");
            user.getWriter().flush();
        } catch (IOException e) {
            exitUser(user);
            user.disconnectUserByServer();
            LOGGER.error("Problem with send message from server to user " + e.getMessage());
        }
    }

    /**
     * Method that use for send message to opponent from server
     *
     * @param user    - client or agent who send the message
     * @param value - user's message to opponent
     */
    private synchronized void sendServerMessageToOpponent(User user, String value) {
        String data;
        LocalDateTime time;
        try {
            time = LocalDateTime.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            data = time.format(formatter);
            if (user.getOpponent() != null) {
                user.getOpponent().getWriter().write("Server " + "(" + data + ")" + " " + value + "\r\n");
                user.getOpponent().getWriter().flush();
            }
        } catch (IOException e) {
            exitUser(user);
            user.disconnectUserByServer();
            LOGGER.error("Problem with send message from server to user " + e.getMessage());
        }
    }

    /**
     * Method that send messages to agent from user's messages storage
     *
     * @param user    - client who has the history of messages
     */

    public void sendMessagesHistoryToAgent(User user) {
        if (user.getMessages().size() > 0 && user.getOpponent() != null) {
            List<String> messages = user.getMessages();
            messages.forEach(offlineMessage -> sendMessageToOpponent(user, offlineMessage));
            user.getMessages().clear();
        }
    }

    /**
     * Method disconnect user from opponent, return opponent to need collections, set opponent=null, set inConversation = false
     *
     * @param user - agent or client who exit the chat
     */
    public void exitUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        switch (user.getRole().toString()) {
            case "AGENT":
                exitAgent(user);
                break;
            case "CLIENT":
                exitClient(user);
                break;
        }
    }

    /**
     * Method disconnect client from opponent, return opponent to need collections, set opponent=null, set inConversation = false
     *
     * @param user - agent who exit the chat
     */
    private void exitAgent(User user) {
        User client = user.getOpponent();
        sendServerMessageToOpponent(user, "Agent with name " + user.getName() + " has left the chat. We will find you a new opponent");
        if (client != null) {
            addUser(client);
            client.setOpponent(null);
            client.setInConversation(false);
            client.setInClientCollection(true);
        }
        user.setUserExit(true);
        removeAgent(user);
        LOGGER.info("User with name" + user.getName() + " with role " + user.getRole() + " have left the chat");
    }

    /**
     * Method disconnect agent from opponent, return opponent to need collections, set opponent=null, set inConversation = false
     *
     * @param user - client who exit the chat
     */
    private void exitClient(User user) {
        sendServerMessageToOpponent(user, "Client with name " + user.getName() + " have left the chat. We will find you a new opponent");
        User agent = user.getOpponent();
        if (agent != null) {
            addAgent(agent);
            agent.setOpponent(null);
            agent.setInConversation(false);
        }
        user.setUserExit(true);
        removeClient(user);
        LOGGER.info("User with name" + user.getName() + " with role " + user.getRole() + " have left the chat");
    }


    /**
     * Method disconnect client from agent, return agent to collections agents for finding a new client
     *
     * @param user - client who leave the chat
     */
    public void disconnectFromAgent(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        sendServerMessageToOpponent(user, "Client with name " + user.getName() + " leave chat. We will find you a new opponent");
        User agent = user.getOpponent();
        addAgent(agent);
        agent.setOpponent(null);
        agent.setInConversation(false);
        user.setOpponent(null);
        user.setInConversation(false);
        user.setInClientCollection(false);
        LOGGER.info("Client with name " + user.getName() + " has left the chat. ");
    }

    /**
     * method connect 2 users (agent and client) to each other
     */
    public void connectToOpponent() {
        User opponent;

        for (User client : clients) {
            while (!client.isUserExit() && client.getOpponent() == null) {
                opponent = getAgent();
                removeClientFromDequeForConversation();
                if (opponent != null) {
                    client.setOpponent(opponent);
                    opponent.setOpponent(client);
                    sendServerMessageToOpponent(client, "Client with name " + client.getName() + " is connected");
                    sendServerMessageToOpponent(opponent, "Agent with name " + opponent.getName() + " is connected");
                    opponent.setInConversation(true);
                    client.setInConversation(true);
                    sendMessagesHistoryToAgent(client);
                    LOGGER.info("Client with name " + client.getName() + " find opponent with name " + opponent.getName());
                }
            }
        }
    }

    /**
     * remove agent from collection
     *
     * @param user - agent
     */
    private void removeAgent(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        agents.remove(user);
    }

    /**
     * remove client from collection
     *
     * @param user - client
     */
    private void removeClient(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        clients.remove(user);
    }

    /**
     * remove client from collection when was found the opponent
     */
    private void removeClientFromDequeForConversation() {
        clients.poll();
    }

    /**
     * Take agent from collection to conversation with client
     *
     * @return User - agent
     */
    private User getAgent() {
        try {
            return agents.take();
        } catch (InterruptedException e) {
            LOGGER.error("Problem with take agent to collection  " + e.getLocalizedMessage());
            return null;
        }
    }

}
