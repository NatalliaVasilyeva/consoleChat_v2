package by.touchsoft.vasilyevanatali.Action;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserIdGenerator {

    private static AtomicInteger idCounter = new AtomicInteger(1);

    public static Integer createID()
    {
        return idCounter.getAndIncrement();
    }

    public static Integer getUserId(){
        return idCounter.intValue();
    }
}
