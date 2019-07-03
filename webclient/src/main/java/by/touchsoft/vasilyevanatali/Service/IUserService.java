package by.touchsoft.vasilyevanatali.Service;

import by.touchsoft.vasilyevanatali.Model.User;

public interface IUserService {

    void addUser(User user);

    void addAgent(User user);

    void addClient(User user);

    void sendServerMessage(String value, User user);

    void sendMessagesHistoryToAgent(User user);

    void exitUser(User user);

    void disconnectFromAgent(User user);

    void connectToOpponent();

}
