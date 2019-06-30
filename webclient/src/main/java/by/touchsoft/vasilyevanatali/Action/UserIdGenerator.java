package by.touchsoft.vasilyevanatali.Action;

import java.util.concurrent.atomic.AtomicInteger;

public class UserIdGenerator {

    private static AtomicInteger idCounter = new AtomicInteger();

    public static Integer createID()
    {
        return idCounter.getAndIncrement();
    }

    public static Integer getUserId(){
        return idCounter.intValue();
    }
}
