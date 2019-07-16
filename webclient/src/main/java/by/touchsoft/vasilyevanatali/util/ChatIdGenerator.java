package by.touchsoft.vasilyevanatali.util;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
/**
 * @author Natali
 * Class for create chat id
 */
@Service
public class ChatIdGenerator {

    /**
     * variable with what begin id
     */
    private static AtomicInteger idCounter = new AtomicInteger(1);

    /**
     *
     * @return id of chatroom
     */
    public static Integer createID() {
        return idCounter.getAndIncrement();
    }

    public static Integer getChatId() {
        return idCounter.intValue();
    }
}
