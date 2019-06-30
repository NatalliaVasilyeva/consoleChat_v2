package by.touchsoft.vasilyevanatali.RestClient;


import by.touchsoft.vasilyevanatali.Chatroom.Chatroom;
import by.touchsoft.vasilyevanatali.Message.ChatMessage;
import by.touchsoft.vasilyevanatali.Repository.ChatRoomRepository;
import by.touchsoft.vasilyevanatali.Repository.UserRepository;
import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UserActionSingleton;
import by.touchsoft.vasilyevanatali.User.UserType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")

public class RestController {

    //Retrieve all registered agents
    @RequestMapping(value = "/agent/all", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllAgents(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<User> allAgents = UserRepository.INSTANCE.getAllAgents();
        if (pageSize != null) {
            if (pageSize > 0) {
                allAgents = allAgents.subList(pageSize * (pageNumber - 1), pageSize * pageNumber);
            }
        }
        return new ResponseEntity<>(allAgents, HttpStatus.OK);
    }


    //Retrieve all free agents
    @RequestMapping(value = "/agent/free", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllFreeAgents(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<User> freeAgents = UserRepository.INSTANCE.getFreeAgents();
        if (pageSize != null) {
            if (pageSize > 0) {
                freeAgents = freeAgents.subList(pageSize * (pageNumber - 1) - 1, pageSize * pageNumber - 1);
            }
        }
        return new ResponseEntity<>(freeAgents, HttpStatus.OK);
    }


    //Retrieve agent his id
    @RequestMapping(value = "/agent/{id}", method = RequestMethod.GET)

    public ResponseEntity<User> getAgentById(@PathVariable("id") int id) {

        User user = UserRepository.INSTANCE.getUserById(id);
        if (user == null || user.getRole() != UserType.AGENT) {
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    //Retrieve count free agents
    @RequestMapping(value = "/agent/count", method = RequestMethod.GET)

    public ResponseEntity<Integer> countFreeAgent() {
        int number = UserRepository.INSTANCE.getFreeAgentsNumber();
        return new ResponseEntity<>(number, HttpStatus.OK);
    }


    //Retrieve all open chats

    @RequestMapping(value = "/chats", method = RequestMethod.GET)
    public ResponseEntity<List<Chatroom>> allChatRooms(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        List<Chatroom> allChatRooms = new ArrayList<>(ChatRoomRepository.INSTANCE.getAllChatRoom());
        if (pageSize != null) {
            if (pageSize > 0) {
                allChatRooms = allChatRooms.subList(pageSize * (pageNumber - 1) - 1, pageSize * pageNumber - 1);
            }
        }
        return new ResponseEntity<>(allChatRooms, HttpStatus.OK);
    }


    //Retrieve single chatRoom by its id

    @RequestMapping(value = "/chat/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> dialog(@PathVariable("id") int id) {
        Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomById(id);
        return new ResponseEntity<>(chatroom, HttpStatus.OK);
    }


    //Retrieve all free clients

    @RequestMapping(value = "/client/queue", method = RequestMethod.GET)
    public ResponseEntity<List<User>> listAllClientInQueue(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<User> clients = UserRepository.INSTANCE.getFreeClients();
        if (pageSize != null) {
            if (pageSize > 0) {
                clients = clients.subList(pageSize * (pageNumber - 1) - 1, pageSize * pageNumber - 1);
            }
        }
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }


    //Retrieve concrete client by id

    @RequestMapping(value = "/client/{id}", method = RequestMethod.GET)
    public ResponseEntity<User> getClient(@PathVariable("id") int id) {
        User client = UserRepository.INSTANCE.getUserById(id);
        if (client == null || client.getRole() != UserType.CLIENT) {
            return new ResponseEntity<>(client, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(client, HttpStatus.OK);
    }


    //Register agent

    @RequestMapping(value = "/register/agent", method = RequestMethod.POST)

    public ResponseEntity<String> registerAgent(@PathVariable("name") String name) {

        ChatMessage message = new ChatMessage(name, LocalDateTime.now(), "/reg agent " + name);
        String username = message.getSenderName() == null ? "" : message.getSenderName();
        String context = message.getText();
        User user = new User();
        user.setRestClient(true);
        String[] splittedFirstMessage = context.split(" ");
        String role = splittedFirstMessage[1];
        user.setRole(UserType.valueOf(role.toUpperCase()));
        user.setName(username);
        user.setRole(UserType.AGENT);
        user.setUserExit(false);
        UserActionSingleton.INSTANCE.addAgent(user);
        UserRepository.INSTANCE.addUser(user);

        return new ResponseEntity<>("Agent with id " + user.getUserId() + " has been register", HttpStatus.OK);

    }


    //Register client
    @RequestMapping(value = "/register/client", method = RequestMethod.POST)
    public ResponseEntity<String> registerClient(@PathVariable("name") String name) {
        ChatMessage message = new ChatMessage(name, LocalDateTime.now(), "/reg client " + name);
        String clientName = message.getSenderName() == null ? "" : message.getSenderName();
        String context = message.getText();
        User client = new User();
        client.setRestClient(true);
        String[] splittedFirstMessage = context.split(" ");
        String role = splittedFirstMessage[1];
        client.setRole(UserType.valueOf(role.toUpperCase()));
        client.setName(clientName);
        client.setRole(UserType.CLIENT);
        client.setUserExit(false);
        UserActionSingleton.INSTANCE.addClient(client);
        UserRepository.INSTANCE.addUser(client);

        return new ResponseEntity<>("Client with id " + client.getUserId() + " has been register", HttpStatus.OK);

    }




}
