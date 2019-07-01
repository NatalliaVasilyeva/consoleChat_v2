package by.touchsoft.vasilyevanatali.Repository;

import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UserActionSingleton;
import by.touchsoft.vasilyevanatali.User.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

public enum UserRepository {

    INSTANCE;

    private final Logger LOGGER = LogManager.getLogger(UserRepository.class);

    private BlockingDeque<User> allUsers = new LinkedBlockingDeque<>();

    public void addUser(User user) {
        try {
            allUsers.put(user);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }

    public BlockingDeque<User> getAllUsers() {
        return allUsers;

    }

    public List<User> getAllAgents() {
        return allUsers.stream().filter(user -> user.getRole().equals(UserType.AGENT)).collect(Collectors.toList());
    }

    public List<User> getFreeAgents() {

        return new ArrayList<>(UserActionSingleton.INSTANCE.getAgents());

    }

    public List<User> getAllClients() {
        return allUsers.stream().filter(user -> user.getRole().equals(UserType.CLIENT)).collect(Collectors.toList());
    }


    public List<User> getFreeClients() {
        return new ArrayList<>(UserActionSingleton.INSTANCE.getClients());
    }


    public synchronized User getAgentByNameAndRole(String username) {
        synchronized (allUsers) {
            for (User user : allUsers)
                if (user != null && user.getName().equals(username)
                        && user.getRole().equals(UserType.AGENT))
                    return user;
            return null;
        }
    }

    public synchronized User getClientByNameAndRole(String username) {
        synchronized (allUsers) {
            for (User user : allUsers)
                if (user != null && user.getName().equals(username)
                        && user.getRole().equals(UserType.CLIENT))
                    return user;
            return null;
        }
    }

    public synchronized User getUserById(Integer id) {
        synchronized (allUsers) {
            for (User user : allUsers)
                if (user != null && user.getUserId().equals(id))
                    return user;
            return null;
        }
    }

    public int getFreeAgentsNumber() {
        return (int)(UserActionSingleton.INSTANCE.getAgents().stream().count());
    }

    public int getFreeClientsNumber() {
        return (int)(UserActionSingleton.INSTANCE.getClients().stream().count());
    }

}