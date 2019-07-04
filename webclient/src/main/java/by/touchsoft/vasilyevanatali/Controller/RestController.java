package by.touchsoft.vasilyevanatali.Controller;


import by.touchsoft.vasilyevanatali.Command.ConversationCommand;
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
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@org.springframework.web.bind.annotation.RestController
@RequestMapping("/api")

public class RestController {

    //Retrieve all registered agents
    @RequestMapping(value = "/agent/all", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> listAllAgents(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<User> allAgents = UserRepository.INSTANCE.getAllAgents();
        if (pageSize != null) {
            if (pageSize > 0) {
                allAgents = allAgents.subList(pageSize * (pageNumber - 1), pageSize * pageNumber > allAgents.size() ? allAgents.size() : pageSize * pageNumber);

            }
        }

        return new ResponseEntity<>(allAgents, HttpStatus.OK);
    }


    //Retrieve all free agents
    @RequestMapping(value = "/agent/free", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> listAllFreeAgents(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<User> freeAgents = UserRepository.INSTANCE.getFreeAgents();
        if (pageSize != null) {
            if (pageSize > 0) {
                freeAgents = freeAgents.subList(pageSize * (pageNumber - 1), pageSize * pageNumber > freeAgents.size() ? freeAgents.size() : pageSize * pageNumber);
            }
        }
        return new ResponseEntity<>(freeAgents, HttpStatus.OK);
    }


    //Retrieve agent his id
    @RequestMapping(value = "/agent/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<User> getAgentById(@PathVariable("id") int id) {

        User user = UserRepository.INSTANCE.getUserById(id);
        if (user == null || user.getRole() != UserType.AGENT) {
            return new ResponseEntity<>(user, HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    //Retrieve count free agents
    @RequestMapping(value = "/agent/count", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)

    public ResponseEntity<Integer> countFreeAgent() {
        int number = UserRepository.INSTANCE.getFreeAgentsNumber();
        return new ResponseEntity<>(number, HttpStatus.OK);
    }


    //Retrieve all open chats

    @RequestMapping(value = "/chats", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Chatroom>> allChatRooms(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {

        List<Chatroom> allChatRooms = (ChatRoomRepository.INSTANCE.getAllChatRoom()).stream().collect(Collectors.toList());
        if (pageSize != null) {
            if (pageSize > 0) {
                allChatRooms = allChatRooms.subList(pageSize * (pageNumber - 1), pageSize * pageNumber > allChatRooms.size() ? allChatRooms.size() : pageSize * pageNumber);
            }
        }
        return new ResponseEntity<>(allChatRooms, HttpStatus.OK);
    }

    //Retrieve single chatRoom by its id

    @RequestMapping(value = "/chat/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> dialog(@PathVariable("id") int id) {
        Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomById(id);
        return new ResponseEntity<>(chatroom, HttpStatus.OK);
    }


    //Retrieve all free clients

    @RequestMapping(value = "/client/queue", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<User>> listAllClientInQueue(
            @RequestParam(value = "pageNumber", required = false) Integer pageNumber,
            @RequestParam(value = "pageSize", required = false) Integer pageSize) {
        List<User> clients = UserRepository.INSTANCE.getFreeClients();
        if (pageSize != null) {
            if (pageSize > 0) {
                clients = clients.subList(pageSize * (pageNumber - 1), pageSize * pageNumber > clients.size() ? clients.size() : pageSize * pageNumber);
            }
        }
        return new ResponseEntity<>(clients, HttpStatus.OK);
    }

    //Retrieve concrete client by id

    @RequestMapping(value = "/client/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<User> getClient(@PathVariable("id") int id) {
        User client = UserRepository.INSTANCE.getUserById(id);
        if (client == null || client.getRole() != UserType.CLIENT) {
            return new ResponseEntity<>(client, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(client, HttpStatus.OK);
    }


    //Register agent

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


    //Register client
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


    //Send message from agent

    @RequestMapping(value = "/agent/sendMessage", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessageFromAgent(@RequestParam(value = "message") String message,
                                                  @RequestParam(value = "userId") String userId) {

        User agent = UserRepository.INSTANCE.getAgentById(Integer.parseInt(userId));
        if (agent == null) {
            return new ResponseEntity<>("There are no agent with this id ", HttpStatus.OK);
        }
        String name = agent.getName();
        ChatMessage chatMessage = new ChatMessage(name, LocalDateTime.now(), message);
        try {
            String jsonMessage = MessageServiceImpl.INSTANCE.convertToJson(chatMessage);
            ConversationCommand conversationCommand = new ConversationCommand(agent);
            conversationCommand.execute(jsonMessage);
        } catch (IOException e) {

        }
        if (agent.getOpponent() == null) {
            return new ResponseEntity<>("Message has been add to list with messages while we find you the client", HttpStatus.OK);
        }
        return new ResponseEntity<>("Message has been sent to opponent", HttpStatus.OK);

    }

    //Send message from client
    @RequestMapping(value = "/client/sendMessage", method = RequestMethod.POST)
    public ResponseEntity<?> sendMessageFromClient(@RequestParam(value = "message") String message,
                                                   @RequestParam(value = "userId") String userId) {

        User client = UserRepository.INSTANCE.getClientById(Integer.parseInt(userId));
        if (client == null) {
            return new ResponseEntity<>("There are no client with this id ", HttpStatus.OK);
        }
        String name = client.getName();
        ChatMessage chatMessage = new ChatMessage(name, LocalDateTime.now(), message);
        try {
            String jsonMessage = MessageServiceImpl.INSTANCE.convertToJson(chatMessage);
            ConversationCommand conversationCommand = new ConversationCommand(client);
            conversationCommand.execute(jsonMessage);
        } catch (IOException e) {

        }
        if (client.getOpponent() == null) {
            return new ResponseEntity<>("You has been added to client's queue. Message has been add to list with messages while we find you the agent", HttpStatus.OK);
        }
        return new ResponseEntity<>("Message has been sent to opponent", HttpStatus.OK);
    }


    //Receive message
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


    //Leave chat
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


    //Exit chat
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
