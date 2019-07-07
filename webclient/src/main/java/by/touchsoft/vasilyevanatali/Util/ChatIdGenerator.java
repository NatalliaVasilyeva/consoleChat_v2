package by.touchsoft.vasilyevanatali.Util;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ChatIdGenerator {

    private static AtomicInteger idCounter = new AtomicInteger(1);

    public static Integer createID() {
        return idCounter.getAndIncrement();
    }

    public static Integer getChatId() {
        return idCounter.intValue();
    }
}
