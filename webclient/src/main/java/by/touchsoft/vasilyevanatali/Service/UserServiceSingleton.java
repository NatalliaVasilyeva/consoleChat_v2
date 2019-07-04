package by.touchsoft.vasilyevanatali.Service;

import by.touchsoft.vasilyevanatali.Model.Chatroom;
import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Repository.ChatRoomRepository;
import by.touchsoft.vasilyevanatali.Repository.UserRepository;
import by.touchsoft.vasilyevanatali.User.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.*;

public enum UserServiceSingleton implements IUserService {
    INSTANCE;

    private final Logger LOGGER = LogManager.getLogger(UserServiceSingleton.class);
    private BlockingDeque<User> clients = new LinkedBlockingDeque<>();
    private BlockingQueue<User> agents = new ArrayBlockingQueue<>(100);

    public BlockingQueue<User> getAgents() {
        return agents;
    }

    public BlockingDeque<User> getClients() {
        return clients;
    }

    private final Object monitor = new Object();

    @Override
    public void addUser(User user) {
        if (user.getRole().equals(UserType.CLIENT)) {
            addClient(user);
        } else {
            addAgent(user);
        }
    }

    @Override
    public void addAgent(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        try {
            agents.put(user);
        } catch (InterruptedException e) {
            LOGGER.error("Problem with adding user to agent collection " + e.getLocalizedMessage());
        }
    }

    @Override
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

    public void sendMessageToOpponent(User user, ChatMessage message) {

        try {
            if (!user.getOpponent().isRestClient()) {
                user.getOpponent().getWriter().write(MessageServiceImpl.INSTANCE.convertToJson(message));
                user.getOpponent().getWriter().newLine();
                user.getOpponent().getWriter().flush();
            } else {
                Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomByUser(user.getOpponent());

                    chatroom.addMessage(message);

            }
        } catch (IOException e) {
            exitUser(user);
            user.disconnectUserByServer();
            LOGGER.error("Problem with sending message to opponent  " + e.getLocalizedMessage());
        }
    }

    @Override
    public synchronized void sendServerMessage(String message, User user) {
        System.out.println("sendServerMessage");
        try {
            ChatMessage messageFromServer = new ChatMessage("Server", LocalDateTime.now(), message);
            if (!user.isRestClient()) {
                user.getWriter().write(MessageServiceImpl.INSTANCE.convertToJson(messageFromServer));
                user.getWriter().newLine();
                user.getWriter().flush();
            } else {
                Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomByUser(user);
                chatroom.addMessage(messageFromServer);
            }
        } catch (IOException e) {
            exitUser(user);
            user.disconnectUserByServer();
            LOGGER.error("Problem with send message from server to user " + e.getMessage());
        }
    }

    private synchronized void sendServerMessageToOpponent(User user, ChatMessage chatMessage) {
        try {
            if (user.getOpponent() != null) {
                if (!user.getOpponent().isRestClient()) {
                    user.getOpponent().getWriter().write(MessageServiceImpl.INSTANCE.convertToJson(chatMessage));
                    user.getOpponent().getWriter().newLine();
                    user.getOpponent().getWriter().flush();
                } else {
                    Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomByUser(user.getOpponent());
                    if (chatroom != null) {
                        chatroom.addMessage(chatMessage);
                    }
                }
            }
        } catch (IOException e) {
            exitUser(user);
            user.disconnectUserByServer();
            LOGGER.error("Problem with send message from server to user " + e.getMessage());
        }
    }

    @Override
    public void sendMessagesHistoryToAgent(User user) {
         if (user.getMessages().size() > 0 && user.getOpponent() != null) {
             List<ChatMessage> messages = user.getMessages();
             if (!user.getOpponent().isRestClient()){
                 messages.forEach((ChatMessage offlineMessage) -> {
                     sendMessageToOpponent(user, offlineMessage);
                 });
         } else {
                 Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomByUser(user.getOpponent());
                 ConcurrentLinkedDeque<ChatMessage> messageOfRestClient = chatroom.getMessages();
                 messageOfRestClient.addAll(messages);
             }
           messages.clear();
        }
    }

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
        UserRepository.INSTANCE.getAllAgents().remove(user);
        LOGGER.info("User with name" + user.getName() + " with role " + user.getRole() + " have left the chat");
    }


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
        UserRepository.INSTANCE.getAllClients().remove(user);
        LOGGER.info("User with name" + user.getName() + " with role " + user.getRole() + " have left the chat");
    }

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

    private void removeAgent(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        agents.remove(user);
    }

    private void removeClient(User user) {
        if (user == null) {
            throw new IllegalArgumentException();
        }
        clients.remove(user);
    }

    private void removeClientFromDequeForConversation() {
        clients.poll();
    }

    private User getAgent() {
        try {
            return agents.take();
        } catch (InterruptedException e) {
            LOGGER.error("Problem with take agent to collection  " + e.getLocalizedMessage());
            return null;
        }
    }

    private void clearChatRoomAfterLeaveOrExit(User user) {
        Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomByUser(user);
        if (chatroom != null) {
            chatroom.setAgent(null);
            chatroom.setClient(null);
            ChatRoomRepository.INSTANCE.getAllChatRoom().remove(chatroom);
            chatroom.getMessages().clear();
        }
    }
}
