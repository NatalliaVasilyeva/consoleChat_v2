package by.touchsoft.vasilyevanatali.Controller;

import by.touchsoft.vasilyevanatali.Command.ExitCommand;
import by.touchsoft.vasilyevanatali.Command.LeaveCommand;
import by.touchsoft.vasilyevanatali.Command.RegisterCommand;
import by.touchsoft.vasilyevanatali.Model.Chatroom;
import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Repository.ChatRoomRepository;
import by.touchsoft.vasilyevanatali.Repository.UserRepository;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Service.MessageServiceImpl;
import by.touchsoft.vasilyevanatali.User.UserType;
import by.touchsoft.vasilyevanatali.Util.CommandStarter;
import by.touchsoft.vasilyevanatali.Util.PaginationAnswer;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * @author Natali
 * Controller that contain method for rest users
 *
 */
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")

public class RestController {


    /**
     * Retrieve all registered agents from page number pageNumber with number of agents per page= pageSize
     *
     * @param pageNumber - page's number/ For example, 1, 2, 3
     * @param pageSize   - size of page
     * @return list of agents
     */

    @RequestMapping(value = "/agent/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> listAllAgents(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<User> allAgents = UserRepository.INSTANCE.getAllAgents();
        if (pageSize != null) {
            if (pageSize > 0) {
                PaginationAnswer.INSTANCE.takeObjectList(allAgents, pageSize, pageNumber);
            }
        }

        return new ResponseEntity<>(allAgents, HttpStatus.OK);
    }


    /**
     * Retrieve all free agents from page number pageNumber with number of agents per page= pageSize
     *
     * @param pageNumber - page's number/ For example, 1, 2, 3
     * @param pageSize   - size of page
     * @return list of agents
     */

    @RequestMapping(value = "/agent/free", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> listAllFreeAgents(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<User> freeAgents = UserRepository.INSTANCE.getFreeAgents();
        if (pageSize != null) {
            if (pageSize > 0) {
                PaginationAnswer.INSTANCE.takeObjectList(freeAgents, pageSize, pageNumber);
            }
        }
        return new ResponseEntity<>(freeAgents, HttpStatus.OK);
    }


    /**
     * Method retutn all information about agent with id {id}
     *
     * @param id = id of agent
     * @return Agent
     */
    @RequestMapping(value = "/agent/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<User> getAgentById(@PathVariable("id") int id) {

        User user = UserRepository.INSTANCE.getUserById(id);
        if (user == null || user.getRole() != UserType.AGENT) {
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    /**
     * Method return number of free agent
     *
     * @return number of free agent
     */
    @RequestMapping(value = "/agent/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<Integer> countFreeAgent() {
        int number = UserRepository.INSTANCE.getFreeAgentsNumber();
        return new ResponseEntity<>(number, HttpStatus.OK);
    }


    /**
     * Method return information about all active chat rooms
     *
     * @param pageNumber - page's number/ For example, 1, 2, 3
     *                   * @param pageSize - size of page
     * @return list of all active chat rooms
     */

    @RequestMapping(value = "/chats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Chatroom>> allChatRooms(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        List<Chatroom> allChatRooms = new ArrayList<>((ChatRoomRepository.INSTANCE.getAllChatRoom()));
        if (pageSize != null) {
            if (pageSize > 0) {
                PaginationAnswer.INSTANCE.takeObjectList(allChatRooms, pageSize, pageNumber);
            }
        }
        return new ResponseEntity<>(allChatRooms, HttpStatus.OK);
    }


    /**
     * Method return information about chat room with id = {id}
     *
     * @param id - chat id
     * @return chatroom
     */

    @RequestMapping(value = "/chat/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> dialog(@PathVariable("id") int id) {
        Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomById(id);
        return new ResponseEntity<>(chatroom, HttpStatus.OK);
    }


    /**
     * Retrieve all free clients from page number pageNumber with number of agents per page= pageSize
     *
     * @param pageNumber - page's number/ For example, 1, 2, 3
     * @param pageSize   - size of page
     * @return list of clients
     */

    @RequestMapping(value = "/client/queue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> listAllClientInQueue(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<User> clients = UserRepository.INSTANCE.getFreeClients();
        if (pageSize != null) {
            if (pageSize > 0) {
                PaginationAnswer.INSTANCE.takeObjectList(clients, pageSize, pageNumber);
            }
        }
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    /**
     * Method retutn all information about client with id {id}
     *
     * @param id = id of client
     * @return Client
     */

    @RequestMapping(value = "/client/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getClient(@PathVariable("id") int id) {
        User client = UserRepository.INSTANCE.getUserById(id);
        if (client == null || client.getRole() != UserType.CLIENT) {
            return new ResponseEntity<>(client, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(client, HttpStatus.OK);
    }


    /**
     * Method register agent in this program
     *
     * @param name - name of agent
     * @return - information about successful registration
     * @throws JsonProcessingException
     */

    @RequestMapping(value = "/register/agent{name}", method = RequestMethod.POST)

    public ResponseEntity<String> registerAgent(@PathVariable("name") String name) throws JsonProcessingException {

        ChatMessage message = new ChatMessage(name, LocalDateTime.now(), "/reg agent " + name);
        String json = MessageServiceImpl.INSTANCE.convertToJson(message);
        User agent = new User();
        agent.setRestClient(true);
        RegisterCommand registerCommand = new RegisterCommand(agent);
        registerCommand.execute(json);

        return new ResponseEntity<>("Agent with id " + agent.getUserId() + " has been register", HttpStatus.OK);

    }


    /**
     * Method register client in this program
     *
     * @param name - name of client
     * @return - information about successful registration
     * @throws JsonProcessingException
     */
    @RequestMapping(value = "/register/client{name}", method = RequestMethod.POST)
    public ResponseEntity<String> registerClient(@PathVariable("name") String name) throws JsonProcessingException {
        ChatMessage message = new ChatMessage(name, LocalDateTime.now(), "/reg client " + name);
        String json = MessageServiceImpl.INSTANCE.convertToJson(message);
        User client = new User();
        client.setRestClient(true);
        RegisterCommand registerCommand = new RegisterCommand(client);
        registerCommand.execute(json);

        return new ResponseEntity<>("Client with id " + client.getUserId() + " has been register", HttpStatus.OK);

    }

    /**
     * Method help to send message from agent to client
     *
     * @param message - message from agent
     * @param userId  - agent id (who sends message)
     * @return - information about successful or not sending
     */
    @RequestMapping(value = "/agent/sendMessage", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessageFromAgent(@RequestParam(value = "message") String message,
                                                  @RequestParam(value = "userId") String userId) {

        User agent = UserRepository.INSTANCE.getAgentById(Integer.parseInt(userId));
        if (agent == null) {
            return new ResponseEntity<>("There are no agent with this id ", HttpStatus.OK);
        }
        String name = agent.getName();
        new CommandStarter().commandStarterInRestController(name, agent, message);
        if (agent.getOpponent() == null) {
            return new ResponseEntity<>("Message has been add to list with messages while we find you the client", HttpStatus.OK);
        }
        return new ResponseEntity<>("Message has been sent to opponent", HttpStatus.OK);

    }

    /**
     * Method help to send message from client to agent
     *
     * @param message - message from client
     * @param userId  - client id (who sends message)
     * @return - information about successful or not sending
     */
    @RequestMapping(value = "/client/sendMessage", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessageFromClient(@RequestParam(value = "message") String message,
                                                   @RequestParam(value = "userId") String userId) {

        User client = UserRepository.INSTANCE.getClientById(Integer.parseInt(userId));
        if (client == null) {
            return new ResponseEntity<>("There are no client with this id ", HttpStatus.OK);
        }
        String name = client.getName();
        new CommandStarter().commandStarterInRestController(name, client, message);
        if (client.getOpponent() == null) {
            return new ResponseEntity<>("You has been added to client's queue. Message has been add to list with messages while we find you the agent", HttpStatus.OK);
        }
        return new ResponseEntity<>("Message has been sent to opponent", HttpStatus.OK);
    }


    /**
     * Method that help to take message from chat room
     *
     * @param userId - user, who want to take message
     * @return -message or list of messages
     */
    @RequestMapping(value = "/receiveMessage", method = RequestMethod.GET)
    public ResponseEntity<?> receiveMessage(@RequestParam(value = "userId") String userId) {

        User user = UserRepository.INSTANCE.getUserById(Integer.parseInt(userId));

        if (user == null) {
            return new ResponseEntity<>("There are no client with this id ", HttpStatus.OK);
        }

        Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomByUser(user);
        if (chatroom == null) {
            return new ResponseEntity<>("There are no active chat room with this user", HttpStatus.OK);
        }
        List<ChatMessage> messages = new ArrayList<>(chatroom.getMessages());
        chatroom.getMessages().clear();

        return new ResponseEntity<>(messages, HttpStatus.OK);
    }


    /**
     * Leave chat by client
     *
     * @param userId - user id, who want to leave chat
     * @return - result of leave chat
     */
    @RequestMapping(value = "/leaveChat", method = RequestMethod.GET)
    public ResponseEntity<?> leaveChat(@RequestParam(value = "userId") String userId) {

        User user = UserRepository.INSTANCE.getClientById(Integer.parseInt(userId));
        if (user == null) {
            return new ResponseEntity<>("There are no user with this id ", HttpStatus.OK);
        }
        String name = user.getName();
        ChatMessage chatMessage = new ChatMessage(name, LocalDateTime.now(), "/leave");
        try {
            String jsonMessage = MessageServiceImpl.INSTANCE.convertToJson(chatMessage);
            LeaveCommand leaveCommand = new LeaveCommand(user);
            leaveCommand.execute(jsonMessage);
        } catch (IOException e) {
            return new ResponseEntity<>("Error with leave chat", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>("You has left chat", HttpStatus.OK);
    }


    /**
     * Leave chat by user
     *
     * @param userId - user id, who want to exit chat
     * @return - result of exit chat
     */
    @RequestMapping(value = "/exitChat", method = RequestMethod.GET)
    public ResponseEntity<?> exitChat(@RequestParam(value = "userId") String userId) {

        User user = UserRepository.INSTANCE.getUserById(Integer.parseInt(userId));
        if (user == null) {
            return new ResponseEntity<>("There are no user with this id ", HttpStatus.OK);
        }
        String name = user.getName();
        ChatMessage chatMessage = new ChatMessage(name, LocalDateTime.now(), "/exit");
        try {
            String jsonMessage = MessageServiceImpl.INSTANCE.convertToJson(chatMessage);
            ExitCommand exitCommand = new ExitCommand(user);
            exitCommand.execute(jsonMessage);
        } catch (IOException e) {
            return new ResponseEntity<>("Error with exit chat", HttpStatus.EXPECTATION_FAILED);
        }

        return new ResponseEntity<>("You has exited chat", HttpStatus.OK);
    }

}
