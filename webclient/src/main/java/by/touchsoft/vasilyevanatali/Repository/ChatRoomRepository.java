package by.touchsoft.vasilyevanatali.Repository;


import by.touchsoft.vasilyevanatali.Model.Chatroom;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Enum.UserRole;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * @author Natali
 * Class ENUM contains methods for get different information about chat room
 */

public enum ChatRoomRepository {

    INSTANCE;

    /**
     * Logger for log information in this class
     */
    private final Logger LOGGER = LogManager.getLogger(ChatRoomRepository.class);

    /**
     * Concurrent collections to keep all chat rooms
     */

    private BlockingDeque<Chatroom> allChatRooms = new LinkedBlockingDeque<>();


    /**
     * Add chat room to collections
     *
     * @param chatroom - chat room
     */
    public void addChatRoom(Chatroom chatroom) {
        try {
            allChatRooms.put(chatroom);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }

    /**
     * Method returns collections with chat rooms
     *
     * @return list of chat rooms
     */
    public BlockingDeque<Chatroom> getAllChatRoom() {
        return allChatRooms;

    }

    /**
     * Method returns list of chat rooms, where is client in
     *
     * @param client - user
     * @return list of chat rooms
     */

    public List<Chatroom> getListChatRoomByClient(User client) {
        return allChatRooms.stream().filter(chatroom -> chatroom.getClient().equals(client)).collect(Collectors.toList());
    }

    /**
     * Method returns list of chat rooms, where is agent  in
     *
     * @param agent - user
     * @return list of chat rooms
     */
    public List<Chatroom> getListChatRoomByAgent(User agent) {
        return allChatRooms.stream().filter(chatroom -> chatroom.getAgent().equals(agent)).collect(Collectors.toList());
    }


    /**
     * Method returns list of chat rooms, where is user in
     *
     * @param user - user
     * @return list of chat rooms
     */
    public List<Chatroom> getListChatRoomByUser(User user) {
        UserRole userType = user.getRole();
        if (userType.equals(UserRole.CLIENT)) {
            return getListChatRoomByClient(user);
        } else {
            return getListChatRoomByAgent(user);
        }
    }

    /**
     * Method returns chat room by id
     *
     * @param id - chat room id
     * @return chat room
     */
    public Chatroom getChatRoomById(Integer id) {
        synchronized (allChatRooms) {
            for (Chatroom chatroom : allChatRooms)
                if (chatroom != null && chatroom.getId() == id)
                    return chatroom;
            return null;
        }
    }


    /**
     * Method returns chat room by client
     *
     * @param client - user
     * @return - chat room
     */
    public Chatroom getChatRoomByClient(User client) {
        synchronized (allChatRooms) {
            for (Chatroom chatroom : allChatRooms)
                if (chatroom.getClient().equals(client))
                    return chatroom;
            return null;
        }
    }

    /**
     * Method returns chat room by agent
     *
     * @param agent - user
     * @return - chat room
     */
    public Chatroom getChatRoomByAgent(User agent) {
        synchronized (allChatRooms) {
            for (Chatroom chatroom : allChatRooms)
                if (chatroom.getClient().equals(agent))
                    return chatroom;
            return null;
        }
    }


    /**
     * Method returns chat room by user
     *
     * @param user - user
     * @return - chat room
     */
    public Chatroom getChatRoomByUser(User user) {
        UserRole userType = user.getRole();
        if (userType.equals(UserRole.CLIENT)) {
            synchronized (allChatRooms) {
                for (Chatroom chatroom : allChatRooms)
                    if (chatroom.getClient().equals(user))
                        return chatroom;
                return null;
            }
        } else {
            synchronized (allChatRooms) {
                for (Chatroom chatroom : allChatRooms)
                    if (chatroom.getAgent().equals(user))
                        return chatroom;
                return null;
            }
        }
    }
}
