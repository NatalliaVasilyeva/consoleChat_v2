package by.touchsoft.vasilyevanatali.Command;

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

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LeaveCommandTest {

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
    public void execute() {
        User client = new User(socket, "Petia", UserType.CLIENT);
        usersAction.addUser(client);
        User agent = new User(socket, "Vania", UserType.AGENT);
        usersAction.addUser(agent);
        client.setInConversation(true);
        agent.setInConversation(true);
        client.setOpponent(agent);
        agent.setOpponent(client);
        LeaveCommand leaveCommand = new LeaveCommand(client, usersAction);
        leaveCommand.execute("/leave");
        Assert.assertNull(agent.getOpponent());
    }
}