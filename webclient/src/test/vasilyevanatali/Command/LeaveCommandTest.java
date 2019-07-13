package vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.Model.Chatroom;
import by.touchsoft.vasilyevanatali.Service.UserServiceSingleton;
import org.junit.Before;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LeaveCommandTest {

    private Socket socket;
    private UserServiceSingleton usersAction;
    private Chatroom chatroom;

    @Before
    public void setUp() throws IOException {
        usersAction = UserServiceSingleton.INSTANCE;
        socket = mock(Socket.class);
        chatroom = mock(Chatroom.class);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("hello".getBytes());
        when(socket.getInputStream()).thenReturn(byteArrayInputStream);
    }
//
//    @Test
//    public void execute() {
//        User client = new User(socket, "Petia", UserType.CLIENT);
//        usersAction.addUser(client);
//        User agent = new User(socket, "Vania", UserType.AGENT);
//        usersAction.addUser(agent);
//        client.setInConversation(true);
//        agent.setInConversation(true);
//        client.setOpponent(agent);
//        agent.setOpponent(client);
//        chatroom.setClient(client);
//        chatroom.setAgent(agent);
//        LeaveCommand leaveCommand = new LeaveCommand(client);
//        leaveCommand.execute("/leave");
//        Assert.assertNull(agent.getOpponent());
//    }
}