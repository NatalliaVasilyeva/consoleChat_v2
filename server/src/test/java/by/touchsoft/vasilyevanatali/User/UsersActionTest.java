package by.touchsoft.vasilyevanatali.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.*;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsersActionTest {
    private Socket socket;
    private UsersAction usersAction;

    @Before
    public void setUp() throws IOException {
        usersAction = new UsersAction();
        socket = mock(Socket.class);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("hello".getBytes());
        when(socket.getInputStream()).thenReturn(byteArrayInputStream);
    }


    @Test
    public void addUserTest_true() {
        User user = new User(socket, "", "agent");
        usersAction.addUser(user);
        BlockingQueue<User> actualAgents = new ArrayBlockingQueue<>(1);
        actualAgents.add(user);
        Assert.assertEquals(usersAction.getAgents().size(), actualAgents.size());

    }

    @Test
    public void connectToOpponentTest_true() {
        User client = new User(socket, "Vasia", "client");
        User agent = new User(socket, "Petia", "agent");
        usersAction.addUser(agent);
        usersAction.addUser(client);
        usersAction.connectToOpponent();

        Assert.assertTrue(client.getOpponent().getName(), true);
    }

    @Test
    public void sendMessageToOpponentTest_true() throws IOException {
        User client = new User(socket, "vasia", "client");
        User agent = new User(socket, "petia", "agent");
        usersAction.addUser(agent);
        usersAction.addUser(client);
        usersAction.connectToOpponent();
        usersAction.sendMessageToOpponent(client, "hello");
        int n = agent.getSocket().getInputStream().available();
        byte[] bytes = new byte[n];
        agent.getSocket().getInputStream().read(bytes, 0, n);
        String s = new String(bytes, StandardCharsets.UTF_8);
        Assert.assertEquals("hello", s);


    }

    @Test
    public void exitUserTest_true() {
        User client = new User(socket, "vasia", "client");
        User agent = new User(socket, "petia", "agent");
        usersAction.addUser(agent);
        usersAction.addUser(client);
        usersAction.connectToOpponent();
        usersAction.exitUser(client);
        Assert.assertNull(agent.getOpponent());


    }

    @Test
    public void disconnectFromAgent() {
        User client = new User(socket, "vasia", "client");
        User agent = new User(socket, "petia", "agent");
        usersAction.addUser(agent);
        usersAction.addUser(client);
        usersAction.connectToOpponent();
        usersAction.disconnectFromAgent(client);
        Assert.assertNull(agent.getOpponent());

    }
}