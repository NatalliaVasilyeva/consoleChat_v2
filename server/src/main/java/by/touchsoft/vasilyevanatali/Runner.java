package by.touchsoft.vasilyevanatali;


import by.touchsoft.vasilyevanatali.User.UsersAction;

/**
 * @author Natali
 * Start server application
 */
public class Runner {

    public static void main(String[] args) {

        new Server(new UsersAction());

    }
}