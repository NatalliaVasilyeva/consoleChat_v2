package by.touchsoft.vasilyevanatali;


import by.touchsoft.vasilyevanatali.Thread.ConnectOpponentThread;
import by.touchsoft.vasilyevanatali.User.UsersAction;

public class Runner {

public static void main(String[] args) {

    UsersAction usersAction = new UsersAction();
    new ConnectOpponentThread(usersAction).start();
    new Server(usersAction);

}
}