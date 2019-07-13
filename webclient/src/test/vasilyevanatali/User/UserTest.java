package vasilyevanatali.User;

import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Enum.UserRole;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.Socket;
import java.time.LocalDateTime;

import static org.junit.Assert.assertTrue;
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
        user = new User(socket, "vasia", UserRole.CLIENT);
    }

    @Test
    public void getMessagesTest_true() {
        User client = new User(socket, "Vasia", UserRole.CLIENT);
        client.getMessages().add(new ChatMessage("Server", LocalDateTime.now(), "hello"));
        Assert.assertEquals("hello", client.getMessages().get(0).getText());
    }

    @Test
    public void disconnectUserTest_true() {
        user.disconnectUserByServer();
        assertTrue(true);
    }
}