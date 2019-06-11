package by.touchsoft.vasilyevanatali;


import by.touchsoft.vasilyevanatali.User.UsersAction;

public class Runner {

    public static void main(String[] args) {

        new Server(new UsersAction());

    }
}