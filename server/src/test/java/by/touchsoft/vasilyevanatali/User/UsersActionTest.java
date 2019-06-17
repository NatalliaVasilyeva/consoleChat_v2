package by.touchsoft.vasilyevanatali.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UsersActionTest {
    private Socket socket;
    UsersAction usersAction;


    @Before

    public void initUsers() throws IOException {
        usersAction = new UsersAction();
        usersAction.setAgents(new ArrayBlockingQueue<>(100));
        usersAction.setClients(new LinkedBlockingDeque<>());
        socket = mock(Socket.class);
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);
        final ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("".getBytes());
        when(socket.getInputStream()).thenReturn(byteArrayInputStream);

    }


    @Test
    public void addUserTest_true() {
        User agent = new User();
        agent.setSocket(socket);
        agent.setName(anyString());
        agent.setRole("agent");
        usersAction.addUser(agent);
        BlockingQueue<User> actualAgents = new ArrayBlockingQueue<>(100);
        actualAgents.add(agent);
        Assert.assertEquals(usersAction.getAgents().size(), actualAgents.size());

    }


    @Test
    public void addAgentTest_true() {
        User agent = new User();
        agent.setSocket(socket);
        agent.setName(anyString());
        agent.setRole("agent");
        usersAction.addAgent(agent);
        BlockingQueue<User> actualAgents = new ArrayBlockingQueue<>(100);
        actualAgents.add(agent);
        Assert.assertEquals(usersAction.getAgents().size(), actualAgents.size());
    }

    @Test
    public void addClient() {
        User client = new User();
        client.setSocket(socket);
        client.setName(anyString());
        client.setRole("client");
        usersAction.addClient(client);
        BlockingDeque<User> actualClients = new LinkedBlockingDeque();
        actualClients.add(client);
        Assert.assertEquals(usersAction.getClients().size(), actualClients.size());
    }

    @Test
    public void removeAgent() {
    }

    @Test
    public void removeClient() {
    }

    @Test
    public void removeClientFromDequeForConversation() {
    }

    @Test
    public void getAgent() {
    }

    @Test
    public void connectToOpponent() {
    }
}