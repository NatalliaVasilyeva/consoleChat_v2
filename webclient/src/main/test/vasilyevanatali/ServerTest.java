package vasilyevanatali;

import by.touchsoft.vasilyevanatali.Server;
import by.touchsoft.vasilyevanatali.User.UsersAction;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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