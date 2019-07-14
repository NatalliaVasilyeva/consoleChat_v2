package by.touchsoft.vasilyevanatali.Service;

import by.touchsoft.vasilyevanatali.Model.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;


public interface IUserService {

    void addUser(User user);

    void addAgent(User user);

    void addClient(User user);

    void sendServerMessage(String value, User user) throws IOException;

    void sendMessagesHistoryToAgent(User user);

    void exitUser(User user);

    void disconnectFromAgent(User user);

    void connectToOpponent();

}
