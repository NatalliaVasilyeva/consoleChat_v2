package by.touchsoft.vasilyevanatali.Chatroom;

import by.touchsoft.vasilyevanatali.Action.ChatIdGenerator;
import by.touchsoft.vasilyevanatali.Message.ChatMessage;
import by.touchsoft.vasilyevanatali.User.User;

import java.util.concurrent.ConcurrentLinkedDeque;

public class Chatroom {

    private int id;
    private User agent;
    private User client;

    public ConcurrentLinkedDeque<ChatMessage> messages = new ConcurrentLinkedDeque<>();

    public Chatroom(User agent, User client) {
        this.agent = agent;
        this.client = client;
        this.id = ChatIdGenerator.createID();
    }

    public int getId() {
        return id;
    }

    public User getAgent() {
        return agent;
    }

    public void setAgent(User agent) {
        this.agent = agent;
    }

    public User getClient() {
        return client;
    }

    public void setClient(User client) {
        this.client = client;
    }

    public ConcurrentLinkedDeque<ChatMessage> getMessages() {
        return messages;
    }

    public void addMessage(ChatMessage message) {
        messages.add(message);
    }

}