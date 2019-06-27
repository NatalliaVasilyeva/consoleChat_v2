package by.touchsoft.vasilyevanatali.User;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserTest {
    private Socket socket;
    private User user;

    @Before
    public void setUp() throws Exception {
        socket = mock(Socket.class);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("hello".getBytes());
        when(socket.getInputStream()).thenReturn(byteArrayInputStream);
        user = new User(socket, "vasia", UserType.CLIENT);
    }

    @Test
    public void getMessagesTest_true() {
        User client = new User(socket, "Vasia", UserType.CLIENT);
        client.getMessages().add("hello");
        Assert.assertEquals("hello", client.getMessages().get(0));
    }

    @Test
    public void disconnectUserTest_true() {
        user.disconnectUserByServer();
        assertTrue(true);
    }
 }