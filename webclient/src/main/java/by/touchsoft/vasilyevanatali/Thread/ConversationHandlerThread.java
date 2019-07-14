package by.touchsoft.vasilyevanatali.Thread;

import by.touchsoft.vasilyevanatali.Command.CommandFactory;
import by.touchsoft.vasilyevanatali.Enum.UserType;
import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import by.touchsoft.vasilyevanatali.Model.User;
import by.touchsoft.vasilyevanatali.Model.UserJPA;
import by.touchsoft.vasilyevanatali.Service.MessageServiceImpl;
import by.touchsoft.vasilyevanatali.Service.UserJPAService;
import by.touchsoft.vasilyevanatali.Service.UserServiceSingleton;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * @author Natali
 * Thread that handlers the messages from user
 */
public class ConversationHandlerThread implements Runnable {

    /**
     * LOGGER variable to log handler information.
     */
    private static final Logger LOGGER = LogManager.getLogger(ConversationHandlerThread.class);


    /**
     * Variable user what send message to opponent
     */
    private User user;

    private Socket socket;

    public ConversationHandlerThread(Socket socket) {
        this.socket = socket;
    }

    /**
     * Method start thread, what read input information and depending on this information call need command from command factory
     */
    @Override
    public void run() {
        try {
            InputStream inputStream = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
            while (!socket.isClosed()) {
                String message = reader.readLine();
                ChatMessage json= MessageServiceImpl.INSTANCE.parseFromJson(message);
                if (json != null) {
                    if (user == null || user.isUserExit()) {
                        user = UserServiceSingleton.INSTANCE.registerUser(json);
                        user.setSocket(socket);
                        user.setType(UserType.CONSOLE);
                        UserServiceSingleton.INSTANCE.addUserToCollections(user);
                        continue;
                    }
                   CommandFactory commandFactory = new CommandFactory(user);
                    commandFactory.startCommand(json);
                }
            }
        } catch (IOException e) {
            UserServiceSingleton.INSTANCE.exitUser(user);
            user.disconnectUserByServer();
            LOGGER.error("Problem with reading message  " + e.getMessage());
        }
    }
}
