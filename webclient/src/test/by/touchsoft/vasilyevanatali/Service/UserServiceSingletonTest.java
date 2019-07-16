package by.touchsoft.vasilyevanatali.Service;

import by.touchsoft.vasilyevanatali.Enum.UserRole;
import by.touchsoft.vasilyevanatali.Enum.UserType;
import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Model.Chatroom;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Repository.ChatRoomRepository;
import by.touchsoft.vasilyevanatali.Repository.UserRepository;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;



public class UserServiceSingletonTest {

    private UserServiceSingleton usersAction;
    private User agent;
    private User client;
    private BlockingDeque<User> actualClients;


    @Before
    public void setUp() {
        usersAction = UserServiceSingleton.INSTANCE;
        ChatMessage messageAgent = new ChatMessage("Agent", LocalDateTime.now(), "reg agent Vasia");
        agent = usersAction.registerUser(messageAgent);
        ChatMessage messageClient = new ChatMessage("Client", LocalDateTime.now(), "reg client Vasia");
        client = usersAction.registerUser(messageClient);

    }


    @Test
    public void addUserTest_true() {
        User user = new User("Vasia", UserRole.CLIENT);
        usersAction.addUser(user);
        actualClients = new LinkedBlockingDeque<>();
        actualClients.add(user);
        Assert.assertEquals(usersAction.getClients().size(), actualClients.size());
        usersAction.getClients().clear();
        actualClients.clear();
    }

    @Test
    public void sendMessageToOpponentTest_true() {
        client.setType(UserType.REST);
        client.setRestClient(true);
        agent.setType(UserType.REST);
        agent.setRestClient(true);
        usersAction.addUser(agent);
        usersAction.addUser(client);
        usersAction.connectToOpponent();
        Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomByUser(client);
        chatroom.getMessages().clear();
        ChatMessage chatMessage = new ChatMessage("Server", LocalDateTime.now(), "hello");
        usersAction.sendMessageToOpponent(client, chatMessage);
        String actual = chatroom.getMessages().getFirst().getText();
        Assert.assertEquals("hello", actual);
    }

    @Test
    public void sendServerMessage() {
        client.setType(UserType.REST);
        client.setRestClient(true);
        agent.setType(UserType.REST);
        agent.setRestClient(true);
        usersAction.addUser(agent);
        usersAction.addUser(client);
        usersAction.connectToOpponent();
        Chatroom chatroom = ChatRoomRepository.INSTANCE.getChatRoomByUser(client);
        String actual = chatroom.getMessages().getFirst().getText();
        Assert.assertEquals("Client with name Client is connected", actual);
    }


    @Test
    public void exitUser() {
        client.setType(UserType.REST);
        client.setRestClient(true);
        agent.setType(UserType.REST);
        agent.setRestClient(true);
        usersAction.addUser(agent);
        usersAction.addUser(client);
        usersAction.connectToOpponent();
        usersAction.exitUser(client);
        Assert.assertNull(agent.getOpponent());
    }

    @Test
    public void disconnectFromAgent() {
        client.setType(UserType.REST);
        client.setRestClient(true);
        agent.setType(UserType.REST);
        agent.setRestClient(true);
        usersAction.addUser(agent);
        usersAction.addUser(client);
        usersAction.connectToOpponent();
        usersAction.disconnectFromAgent(client);
       Assert.assertNull(agent.getOpponent());
    }

    @Test
    public void connectToOpponent() {

        client.setType(UserType.REST);
        client.setRestClient(true);
        agent.setType(UserType.REST);
        agent.setRestClient(true);
        usersAction.addUser(agent);
        usersAction.addUser(client);
        usersAction.connectToOpponent();
        Assert.assertNotNull(client.getOpponent());
    }


    @Test
    public void addUserToCollections() {
        client.setType(UserType.REST);
        client.setRestClient(true);
        usersAction.addUserToCollections(client);
        Assert.assertEquals(client, UserRepository.INSTANCE.getAllClients().get(0));
    }


}