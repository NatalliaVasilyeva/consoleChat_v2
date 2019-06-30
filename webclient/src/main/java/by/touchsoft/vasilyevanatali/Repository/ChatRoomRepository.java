package by.touchsoft.vasilyevanatali.Repository;


import by.touchsoft.vasilyevanatali.Chatroom.Chatroom;
import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public enum ChatRoomRepository {
    INSTANCE;

    private final Logger LOGGER = LogManager.getLogger(ChatRoomRepository.class);

    private BlockingDeque<Chatroom> allChatRooms = new LinkedBlockingDeque<>();

    public void addChatRoom(Chatroom chatroom) {
        try {
            allChatRooms.put(chatroom);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }

    public BlockingDeque<Chatroom> getAllChatRoom() {
        return allChatRooms;

    }

    public List<Chatroom> getListChatRoomByClient(User client) {
        return allChatRooms.stream().filter(chatroom -> chatroom.getClient().equals(client)).collect(Collectors.toList());
    }

    public List<Chatroom> getListChatRoomByAgent(User agent) {
        return allChatRooms.stream().filter(chatroom -> chatroom.getAgent().equals(agent)).collect(Collectors.toList());
    }

    public List<Chatroom> getListChatRoomByUser(User user) {
        UserType userType = user.getRole();
        if (userType.equals(UserType.CLIENT)) {
            return allChatRooms.stream().filter(chatroom -> chatroom.getClient().equals(user)).collect(Collectors.toList());
        } else {
            return allChatRooms.stream().filter(chatroom -> chatroom.getAgent().equals(user)).collect(Collectors.toList());
        }
    }


    public synchronized Chatroom getChatRoomById(Integer id) {
        synchronized (allChatRooms) {
            for (Chatroom chatroom : allChatRooms)
                if (chatroom != null && chatroom.getId() == id)
                    return chatroom;
            return null;
        }
    }

    public synchronized Chatroom getChatRoomByClient(User client) {
        synchronized (allChatRooms) {
            for (Chatroom chatroom : allChatRooms)
                if (chatroom.getClient().equals(client))
                    return chatroom;
            return null;
        }
    }

    public synchronized Chatroom getChatRoomByAgent(User agent) {
        synchronized (allChatRooms) {
            for (Chatroom chatroom : allChatRooms)
                if (chatroom.getClient().equals(agent))
                    return chatroom;
            return null;
        }
    }

    public synchronized Chatroom getChatRoomByUser(User user) {
        UserType userType = user.getRole();
        if (userType.equals(UserType.CLIENT)) {
            synchronized (allChatRooms) {
                for (Chatroom chatroom : allChatRooms)
                    if (chatroom.getClient().equals(user))
                        return chatroom;
                return null;
            }
        } else {
            synchronized (allChatRooms) {
                for (Chatroom chatroom : allChatRooms)
                    if (chatroom.getClient().equals(user))
                        return chatroom;
                return null;
            }
        }
    }
}
