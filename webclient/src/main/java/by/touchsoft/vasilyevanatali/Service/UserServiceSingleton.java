package by.touchsoft.vasilyevanatali.Service;

import by.touchsoft.vasilyevanatali.Enum.UserRole;
import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Model.Chatroom;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Repository.ChatRoomRepository;
import by.touchsoft.vasilyevanatali.Repository.UserRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.websocket.EncodeException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;



public enum UserServiceSingleton implements IUserService {
    INSTANCE;

    private final Logger LOGGER = LogManager.getLogger(UserServiceSingleton.class);
    /**
     * Clients collection
     */
    private BlockingDeque<User> clients = new LinkedBlockingDeque<>();

    /**
     * Agent collection
     */
    private BlockingQueue<User> agents = new ArrayBlockingQueue<>(100);

    /**
     *
     * @return Queue of agents
     */
    public BlockingQueue<User> getAgents() {
        return agents;
    }

    /**
     *
     * @return Queue of clients
     */
    public BlockingDeque<User> getClients() {
        return clients;
    }

    /**
     * Monitor for synchronised method or collection
     */

    private final Object monitor = new Object();

    @Autowired
    UserJPAService userJPAService;


    /**
     * Add user to need collection
     * @param user - agent or client
     */
    @Override
    public void addUser(User user) {
        if (user.getRole().equals(UserRole.CLIENT)) {
            addClient(user);
        } else {
            addAgent(user);
        }
    }


    /**
     * Add user to agent's collection
     * @param user - agent
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
     *  Add user to client's collection
     * @param user - client
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
     * Method is used for send message from between users
     * @param user - message sender
     * @param message - message for agent or client
     */
    public synchronized void sendMessageToOpponent(User user, ChatMessage message) {
        try {
            switch (user.getOpponent().getType().toString()) {
                case "WEB":
                    user.getOpponent().getSession().getBasicRemote().sendObject(message);
                    break;
                case "REST":
                    Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomByUser(user.getOpponent());
                    message.setReceiverId(user.getOpponent().getUserId());
                    Objects.requireNonNull(chatroom).addMessage(message);
                    break;
                case "CONSOLE":
                    user.getOpponent().getWriter().write(MessageServiceImpl.INSTANCE.convertToJson(message));
                    user.getOpponent().getWriter().newLine();
                    user.getOpponent().getWriter().flush();
                    break;
                default:
                    exitUser(user);
                    user.disconnectUserByServer();
                    LOGGER.error("Problem with sending message to opponent");
                    break;
            }
        } catch (IOException | EncodeException e) {
            exitUser(user);
            user.disconnectUserByServer();
            LOGGER.error("Problem with sending message to opponent");
        }
    }

    /**
     * Method is used for send message from server to users
     * @param message message from server to client or agent
     * @param user - user to whom sends message
     */
    @Override
    public synchronized void sendServerMessage(String message, User user) {
        ChatMessage messageFromServer = new ChatMessage("Server", LocalDateTime.now(), message);
        try {
            switch (user.getType().toString()) {
                case "WEB":
                    user.getSession().getBasicRemote().sendObject(messageFromServer);
                    break;
                case "REST":
                    Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomByUser(user);
                    if (chatroom != null) {
                        messageFromServer.setReceiverId(user.getUserId());
                        chatroom.addMessage(messageFromServer);
                    }
                    break;
                case "CONSOLE":
                    user.getWriter().write(MessageServiceImpl.INSTANCE.convertToJson(messageFromServer));
                    user.getWriter().newLine();
                    user.getWriter().flush();
                    break;
                default:
                    exitUser(user);
                    user.disconnectUserByServer();
                    LOGGER.error("Problem with send message from server to user");
                    break;
            }
        } catch (IOException | EncodeException e) {
            exitUser(user);
            user.disconnectUserByServer();
            LOGGER.info("Problem with send message from server to user " + e.getMessage());
        }
    }


    /**
     * Method is used for send message from server to users opponent
     * @param user - client or agent
     * @param chatMessage - message from server to client or agent
     */
    private synchronized void sendServerMessageToOpponent(User user, ChatMessage chatMessage) {
        sendMessageToOpponent(user, chatMessage);
        LOGGER.info("Server message to opponent");

    }

    /**
     * Method send all client's messages what he has wrote before find agent
     * @param user - client
     */
    private void sendMessagesHistoryToAgent(User user) {
        if (user.getMessages().size() > 0 && user.getOpponent() != null) {
            List<ChatMessage> messages = user.getMessages();
            for(ChatMessage message: messages){
                message.setReceiverId(user.getOpponent().getUserId());
            }
            if (!user.getOpponent().isRestClient()) {
                messages.forEach((ChatMessage offlineMessage) -> sendMessageToOpponent(user, offlineMessage));
            } else {
                Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomByUser(user.getOpponent());
                ConcurrentLinkedDeque<ChatMessage> messageOfRestClient = chatroom.getMessages();
                messageOfRestClient.addAll(messages);
            }
            messages.clear();
        }
    }

    /**
     * Exit user from chat
     * @param user - agent or client
     */
    @Override
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
     * Method disconnect agent from client, remove from collections, release client for another agent
     * @param user - agent
     */
    private void exitAgent(User user) {
        User client = user.getOpponent();
        LocalDateTime time = LocalDateTime.now();
        ChatMessage exitMessage = new ChatMessage("Server", time, "Agent with name " + user.getName() + " has left the chat. We will find you a new opponent");
        if (client != null) {
            sendServerMessageToOpponent(user, exitMessage);
            addUser(client);
            client.setOpponent(null);
            client.setInConversation(false);
            client.setInClientCollection(true);
            clearChatRoomAfterLeaveOrExit(user);
        }
        user.setUserExit(true);
        removeAgent(user);
        UserRepository.INSTANCE.getAllUsers().remove(user);
        LOGGER.info("User with name" + user.getName() + " with role " + user.getRole() + " have left the chat");
    }


    /**
     * Method disconnect client from agent, remove from collections, release agent for another clients
     * @param user - client
     */
    private void exitClient(User user) {
        LocalDateTime time = LocalDateTime.now();
        ChatMessage exitMessage = new ChatMessage("Server", time, "Client with name " + user.getName() + " have left the chat. We will find you a new opponent");
        User agent = user.getOpponent();
        if (agent != null) {
            sendServerMessageToOpponent(user, exitMessage);
            addAgent(agent);
            agent.setOpponent(null);
            agent.setInConversation(false);
            clearChatRoomAfterLeaveOrExit(user);
        }
        user.setUserExit(true);
        removeClient(user);
        UserRepository.INSTANCE.getAllUsers().remove(user);
        LOGGER.info("User with name" + user.getName() + " with role " + user.getRole() + " have left the chat");
    }


    /**
     * Method disconnect client from agent when leave (not exit) chat and chat room
     * @param user - client
     */
    @Override
    public void disconnectFromAgent(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        LocalDateTime time = LocalDateTime.now();
        ChatMessage leaveMessage = new ChatMessage("Server", time, "Client with name " + user.getName() + " leave chat. We will find you a new opponent");
        sendServerMessageToOpponent(user, leaveMessage);
        User agent = user.getOpponent();
        addAgent(agent);
        agent.setOpponent(null);
        agent.setInConversation(false);
        user.setOpponent(null);
        user.setInConversation(false);
        user.setInClientCollection(false);
        clearChatRoomAfterLeaveOrExit(user);

        LOGGER.info("Client with name " + user.getName() + " has left the chat. ");
    }


    /**
     * Method connected agent and client, create chat room with this users
     */
    @Override
    public void connectToOpponent() {
        for (User client : clients) {
            synchronized (monitor) {
                User agent;
                while (!client.isUserExit() && client.getOpponent() == null) {
                    agent = getAgent();
                    removeClientFromDequeForConversation();
                    if (agent != null) {
                        Chatroom chatroom = new Chatroom(agent, client);
                        client.setOpponent(agent);
                        agent.setOpponent(client);
                        ChatRoomRepository.INSTANCE.addChatRoom(chatroom);
                        agent.setInConversation(true);
                        client.setInConversation(true);
                        sendServerMessageToOpponent(client, new ChatMessage("Server", LocalDateTime.now(), "Client with name " + client.getName() + " is connected"));
                        sendServerMessageToOpponent(agent, new ChatMessage("Server", LocalDateTime.now(), "Agent with name " + agent.getName() + " is connected"));
                        sendMessagesHistoryToAgent(client);
                        LOGGER.info("Client with name " + client.getName() + " find opponent with name " + agent.getName());
                    }
                }
                monitor.notify();
            }
        }
    }

    /**
     * Remove agent from online user
     * @param user - agent
     */
    private void removeAgent(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        agents.remove(user);
    }

    /**
     *  Remove client from online users
     * @param user - client
     */
    private void removeClient(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        clients.remove(user);
    }

    /**
     * Take user from collection for conversation
     */
    private void removeClientFromDequeForConversation() {
        clients.poll();
    }

    /**
     * Take agent from collection
     * @return agent
     */
    private User getAgent() {
        try {
            return agents.take();
        } catch (InterruptedException e) {
            LOGGER.error("Problem with take agent to collection  " + e.getLocalizedMessage());
            return null;
        }
    }

    /**
     * Make chat room and user here null
     * @param user - client or agent
     */
    private void clearChatRoomAfterLeaveOrExit(User user) {
        Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomByUser(user);
        if (chatroom != null) {
            chatroom.setAgent(null);
            chatroom.setClient(null);
            ChatRoomRepository.INSTANCE.getAllChatRoom().remove(chatroom);
            chatroom.getMessages().clear();
        }
    }

    /**
     * Create new user
     * @param message - chat message
     * @return - new user
     */
    public User registerUser(ChatMessage message) {

        String username = message.getSenderName() == null ? "" : message.getSenderName();
        String context = message.getText();
        String[] splittedFirstMessage = context.split(" ");
        String role = splittedFirstMessage[1];
        User user = new User(username, UserRole.valueOf(role.toUpperCase()));
         user.setUserExit(false);
        return user;
    }

    /**
     * Method is used to add user to repository for using in rest services
     * @param user - client or agent
     */

    public void addUserToCollections(User user) {

        switch (user.getRole().toString()) {
            case "AGENT":
                UserServiceSingleton.INSTANCE.sendServerMessage("Register was successful. Wait when client send you a message", user);
                UserServiceSingleton.INSTANCE.addUser(user);
                UserRepository.INSTANCE.addUser(user);
                LOGGER.info("Agent " + user.getName() + " has been registered successful");
                break;
            case "CLIENT":
                UserServiceSingleton.INSTANCE.sendServerMessage("Register was successful. Please write you message", user);
                UserRepository.INSTANCE.addUser(user);
                LOGGER.info("Client " + user.getName() + " has been registered successful");
                break;
        }
    }
}
