package by.touchsoft.vasilyevanatali;

import by.touchsoft.vasilyevanatali.User.UsersAction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class ServerTest {
    private UsersAction usersAction;
    private Server server;

    @Before
    public void setUp() {
        usersAction = new UsersAction();
        server = mock(Server.class);
    }

    @Test
    public void getUsersAction() {
        when(server.getUsersAction()).thenReturn(usersAction);
        assertEquals(server.getUsersAction(), usersAction);
    }
}