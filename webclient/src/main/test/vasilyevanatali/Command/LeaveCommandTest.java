package vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.Chatroom.Chatroom;
import by.touchsoft.vasilyevanatali.Command.LeaveCommand;
import by.touchsoft.vasilyevanatali.User.User;
import by.touchsoft.vasilyevanatali.User.UserActionSingleton;
import by.touchsoft.vasilyevanatali.User.UserType;
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
    private UserActionSingleton usersAction;
    private Chatroom chatroom;

    @Before
    public void setUp() throws IOException {
        usersAction = UserActionSingleton.INSTANCE;
        socket = mock(Socket.class);
        chatroom=mock(Chatroom.class);
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
        chatroom.setClient(client);
        chatroom.setAgent(agent);
        LeaveCommand leaveCommand = new LeaveCommand(client);
        leaveCommand.execute("/leave");
        Assert.assertNull(agent.getOpponent());
    }
}