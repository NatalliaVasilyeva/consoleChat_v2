package vasilyevanatali.User;

import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UserType;
import by.touchsoft.vasilyevanatali.User.UsersAction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

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
        User user = new User(socket, "", UserType.AGENT);
        usersAction.addUser(user);
        BlockingQueue<User> actualAgents = new ArrayBlockingQueue<>(1);
        actualAgents.add(user);
        Assert.assertEquals(usersAction.getAgents().size(), actualAgents.size());

    }

    @Test
    public void connectToOpponentTest_true() {
        User client = new User(socket, "Vasia", UserType.CLIENT);
        User agent = new User(socket, "Petia", UserType.AGENT);
        client.setUserExit(false);
        agent.setUserExit(false);
        usersAction.addUser(agent);
        usersAction.addUser(client);
        usersAction.connectToOpponent();

        Assert.assertNotNull(client.getOpponent());
    }

    @Test
    public void sendMessageToOpponentTest_true() throws IOException {
        User client = new User(socket, "vasia", UserType.CLIENT);
        User agent = new User(socket, "petia", UserType.AGENT);
        client.setUserExit(false);
        agent.setUserExit(false);
        usersAction.addUser(agent);
        usersAction.addUser(client);
        usersAction.connectToOpponent();
      //  usersAction.sendMessageToOpponent(client, "hello");
        int n = agent.getSocket().getInputStream().available();
        byte[] bytes = new byte[n];
        agent.getSocket().getInputStream().read(bytes, 0, n);
        String s = new String(bytes, StandardCharsets.UTF_8);
        Assert.assertEquals("hello", s);


    }

    @Test
    public void exitUserTest_true() {
        User client = new User(socket, "vasia", UserType.CLIENT);
        User agent = new User(socket, "petia", UserType.AGENT);
        client.setUserExit(false);
        agent.setUserExit(false);
        usersAction.addUser(agent);
        usersAction.addUser(client);
        usersAction.connectToOpponent();
        usersAction.exitUser(client);
        Assert.assertNull(agent.getOpponent());


    }

    @Test
    public void disconnectFromAgent() {
        User client = new User(socket, "vasia", UserType.CLIENT);
        User agent = new User(socket, "petia", UserType.AGENT);
        client.setUserExit(false);
        agent.setUserExit(false);
        usersAction.addUser(agent);
        usersAction.addUser(client);
        usersAction.connectToOpponent();
        usersAction.disconnectFromAgent(client);
        Assert.assertNull(agent.getOpponent());

    }
}