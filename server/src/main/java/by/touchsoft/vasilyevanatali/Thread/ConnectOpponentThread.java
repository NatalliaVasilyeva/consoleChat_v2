package by.touchsoft.vasilyevanatali.Thread;



import by.touchsoft.vasilyevanatali.User.UsersAction;


public class ConnectOpponentThread extends Thread {

    private UsersAction usersAction;

    public ConnectOpponentThread(UsersAction usersAction){

        this.usersAction=usersAction;
    }



    @Override
    public void run(){

           while(true){
               usersAction.connectToOpponent();
           }
        }

}



