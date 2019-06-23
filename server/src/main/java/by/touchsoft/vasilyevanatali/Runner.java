package by.touchsoft.vasilyevanatali;


import by.touchsoft.vasilyevanatali.User.UsersAction;

/**
 * @author Natali
 * Start server application
 */
public class Runner {

    public static void main(String[] args) {

        /**
         * Start server module
         */
        new Server(new UsersAction());
    }
}