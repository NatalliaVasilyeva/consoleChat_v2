package by.touchsoft.vasilyevanatali.Util;

import by.touchsoft.vasilyevanatali.Command.ConversationCommand;
import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Service.MessageServiceImpl;

import java.io.IOException;
import java.time.LocalDateTime;

public class CommandStarter {

    public void commandStarterInRestController(String name, User user, String message) {
        ChatMessage chatMessage = new ChatMessage(name, LocalDateTime.now(), message);

        //    String jsonMessage = MessageServiceImpl.INSTANCE.convertToJson(chatMessage);
            ConversationCommand conversationCommand = new ConversationCommand(user);
//            conversationCommand.execute(jsonMessage);
            conversationCommand.execute(chatMessage);

    }
}
