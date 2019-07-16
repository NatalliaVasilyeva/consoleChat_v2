package by.touchsoft.vasilyevanatali.Command;

import by.touchsoft.vasilyevanatali.Enum.UserType;
import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Service.UserServiceSingleton;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDateTime;



public class ConversationCommandTest {


    private UserServiceSingleton usersAction;

    @Before
    public void setUp() {
        usersAction = UserServiceSingleton.INSTANCE;
    }

    @Test
    public void executeTest_true() {
        ChatMessage message = new ChatMessage("Server", LocalDateTime.now(), "reg agent Vasia");
        User client = usersAction.registerUser(message);
        client.setType(UserType.REST);
        usersAction.addUser(client);
        ConversationCommand conversationCommand = new ConversationCommand(client);
        conversationCommand.execute(message);
        Assert.assertNotNull(client.getMessages());
    }
}