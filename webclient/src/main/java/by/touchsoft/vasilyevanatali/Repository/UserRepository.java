package by.touchsoft.vasilyevanatali.Repository;

import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Service.UserServiceSingleton;
import by.touchsoft.vasilyevanatali.User.UserType;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.stream.Collectors;

/**
 * @author Natali
 * Class ENUM contains methods for get different information about users
 */
public enum UserRepository {

    INSTANCE;

    /**
     * Logger for log information in this class
     */
    private final Logger LOGGER = LogManager.getLogger(UserRepository.class);

    /**
     * Concurrent collections to keep all users
     */
    private BlockingDeque<User> allUsers = new LinkedBlockingDeque<>();


    /**
     * Add user to collections
     *
     * @param user
     */
    public void addUser(User user) {
        try {
            allUsers.put(user);
        } catch (InterruptedException e) {
            LOGGER.error(e);
        }
    }


    /**
     * Method returns collections with all users
     *
     * @return list of users
     */
    public BlockingDeque<User> getAllUsers() {
        return allUsers;

    }

    /**
     * Method returns collections with agents
     *
     * @return list of all agent
     */

    public List<User> getAllAgents() {
        return allUsers.stream().filter(user -> user.getRole().equals(UserType.AGENT)).collect(Collectors.toList());
    }


    /**
     * Method returns collections with free agents
     *
     * @return list of free agent
     */
    public List<User> getFreeAgents() {

        return new ArrayList<>(UserServiceSingleton.INSTANCE.getAgents());

    }

    /**
     * Method returns collections with all clients
     *
     * @return list of all clients
     */
    public List<User> getAllClients() {
        return allUsers.stream().filter(user -> user.getRole().equals(UserType.CLIENT)).collect(Collectors.toList());
    }


    /**
     * Method returns collections with free clients
     *
     * @return list of free clients
     */
    public List<User> getFreeClients() {
        return new ArrayList<>(UserServiceSingleton.INSTANCE.getClients());
    }


    /**
     * Method returns user by his name and role
     *
     * @param username - name of user
     * @param userType - user role
     * @return User
     */

    public User getUserByNameAndRole(String username, UserType userType) {
        for (User user : allUsers) {
            if (user != null && user.getName().equals(username)
                    && user.getRole().equals(userType))
                return user;
        }
        return null;
    }


    /**
     * Method returns user by his id
     *
     * @param id - user id
     * @return -User
     */
    public User getUserById(Integer id) {
        for (User user : allUsers) {
            if (user != null && user.getUserId().equals(id))
                return user;
        }
        return null;
    }


    /**
     * Method returns agent by his id
     *
     * @param id - agent's id
     * @return -Agent
     */
    public User getAgentById(Integer id) {
        for (User user : allUsers) {
            if (user != null && user.getUserId().equals(id) && user.getRole().equals((UserType.AGENT)))
                return user;
        }
        return null;
    }


    /**
     * Method returns client by his id
     *
     * @param id - client's id
     * @return -Client
     */
    public User getClientById(Integer id) {
        for (User user : allUsers) {
            if (user != null && user.getUserId().equals(id) && user.getRole().equals((UserType.CLIENT)))
                return user;
        }
        return null;
    }

    /**
     * Method return number of free agent
     *
     * @return number of free agent
     */
    public int getFreeAgentsNumber() {
        return UserServiceSingleton.INSTANCE.getAgents().size();
    }


    /**
     * Method return number of waiting clients
     *
     * @return number of waiting clients
     */
    public int getWaitingClientsNumber() {
        return UserServiceSingleton.INSTANCE.getClients().size();
    }

}
