package by.touchsoft.vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.User.User;
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

public class ConversationCommandTest {

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
    public void executeTest_true() {
        User client = new User(socket, "Petia", "client");
        usersAction.addUser(client);
        ConversationCommand conversationCommand = new ConversationCommand(client, usersAction);
        conversationCommand.execute("hello. Help me");
        Assert.assertTrue(client.getMessages() != null);


    }
}