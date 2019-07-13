package vasilyevanatali;

import by.touchsoft.vasilyevanatali.WebClient.ChatEndPoint;
import org.junit.Before;
import org.junit.Test;

import javax.websocket.RemoteEndpoint.Basic;
import javax.websocket.Session;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;

import static org.mockito.Mockito.*;


public class ChatEndPointTest {

    @Before
    public void setUp() throws Exception {
        Socket socket = mock(Socket.class);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        when(socket.getOutputStream()).thenReturn(byteArrayOutputStream);
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream("hello".getBytes());
        when(socket.getInputStream()).thenReturn(byteArrayInputStream);
    }


    @Test
    public void sendMessageToWebPageTest_true() throws IOException {
        ChatEndPoint endPoint = new ChatEndPoint();
        Session session = mock(Session.class);
        endPoint.setSession(session);
        Basic basic = mock(Basic.class);
        when(session.getBasicRemote()).thenReturn(basic);
        endPoint.sendMessageToWebPage("hello");
        verify(session, times(1)).getBasicRemote();
        verify(basic, times(1)).sendText("hello");
    }

}