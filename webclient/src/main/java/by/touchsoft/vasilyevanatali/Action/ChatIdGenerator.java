package by.touchsoft.vasilyevanatali.Action;

import java.util.concurrent.atomic.AtomicInteger;

public class ChatIdGenerator {

    private static AtomicInteger idCounter = new AtomicInteger();

    public static Integer createID() {
        return idCounter.getAndIncrement();
    }

    public static Integer getChatId() {
        return idCounter.intValue();
    }
}
