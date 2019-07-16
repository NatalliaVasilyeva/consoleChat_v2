package by.touchsoft.vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.Enum.UserType;
import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Service.UserServiceSingleton;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;


import java.time.LocalDateTime;


public class ExitCommandTest {

    private UserServiceSingleton usersAction;
    private User client;
   private User agent;

    @Before
    public void setUp()  {
        usersAction = UserServiceSingleton.INSTANCE;
        ChatMessage message = new ChatMessage("Server", LocalDateTime.now(), "reg client Vasia");
        client = usersAction.registerUser(message);
        client.setType(UserType.REST);
        usersAction.addUser(client);
        ChatMessage messageTwo = new ChatMessage("Server", LocalDateTime.now(), "reg agent Vasia");
        agent = usersAction.registerUser(messageTwo);
        agent.setType(UserType.REST);
        usersAction.addUser(agent);

    }

    @Test
    public void execute() {
        ChatMessage message = new ChatMessage("Server", LocalDateTime.now(), "/exit");
        ExitCommand exitCommand = new ExitCommand(client);
        exitCommand.execute(message);
        Assert.assertNull(client.getOpponent());

    }
}