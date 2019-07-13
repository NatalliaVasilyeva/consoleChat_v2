package by.touchsoft.vasilyevanatali.Model;

import by.touchsoft.vasilyevanatali.Util.ChatIdGenerator;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedDeque;


/**
 * @author Natali
 * Chat room contains 2 users who send message each other
 */
public class Chatroom {

    /**
     * Chat room id
     */
    private int id;

    /**
     * Agent
     */
    private User agent;

    /**
     * Client
     */
    private User client;


    /**
     * List of messages to rest client or rest agent
     */
    private ConcurrentLinkedDeque<ChatMessage> messages = new ConcurrentLinkedDeque<>();


    /**
     * List of all chat messages
     */
    private ConcurrentLinkedDeque<ChatMessage> allChatMessages = new ConcurrentLinkedDeque<>();


    /**
     * Date of created
     */

    LocalDateTime createdTime;

    /**
     * Constructor with parameters
     *
     * @param agent
     * @param client
     */
    public Chatroom(User agent, User client) {
        this.agent = agent;
        this.client = client;
        this.id = ChatIdGenerator.createID();
        this.createdTime = LocalDateTime.now();
    }


    /**
     * Method return chat id
     *
     * @return chat id
     */
    public int getId() {
        return id;
    }


    /**
     * Method return agent, who speak in this chat
     *
     * @return agent
     */
    public User getAgent() {
        return agent;
    }


    /**
     * Method set agent to this chat, when chatroom will be create or destroy
     *
     * @param agent
     */
    public void setAgent(User agent) {
        this.agent = agent;
    }

    /**
     * Method return client, who speak in this chat
     *
     * @return client
     */
    public User getClient() {
        return client;
    }

    /**
     * Method set client to this chat, when chatroom will be create or destroy
     *
     * @param client
     */
    public void setClient(User client) {
        this.client = client;
    }


    /**
     * Method return list of messages to rest user
     *
     * @return list of messages
     */
    public ConcurrentLinkedDeque<ChatMessage> getMessages() {
        return messages;
    }


    /**
     * Add message to collections
     *
     * @param message - message for user
     */
    public synchronized void addMessage(ChatMessage message) {
        messages.add(message);
    }



    /**
     * Method return list of messages to rest user
     *
     * @return list of messages
     */
    public ConcurrentLinkedDeque<ChatMessage> getAllMessages() {
        return allChatMessages;
    }


    /**
     * Add message to collections
     *
     * @param message - message for user
     */
    public synchronized void addToAllMessage(ChatMessage message) {
        allChatMessages.add(message);
    }


    @Override
    public String toString() {
        return "Chatroom{" +
                "id=" + id +
                ", agent=" + agent +
                ", client=" + client +
                ", messages=" + messages +
                ", createdTime=" + createdTime +
                '}';
    }
}