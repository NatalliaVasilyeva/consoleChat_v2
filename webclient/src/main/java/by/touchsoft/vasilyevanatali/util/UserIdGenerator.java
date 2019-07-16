package by.touchsoft.vasilyevanatali.util;

import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Natali
 * Class for create user id
 */

@Service
public class UserIdGenerator {

    /**
     * variable with what begin id
     */
    private static AtomicInteger idCounter = new AtomicInteger(1);

    /**
     *
     * @return id of user
     */
    public static Integer createID() {
        return idCounter.getAndIncrement();
    }

    public static Integer getUserId() {
        return idCounter.intValue();
    }
}
