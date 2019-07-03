package by.touchsoft.vasilyevanatali.Command;


import org.springframework.stereotype.Service;

/**
 * @author Natali
 * Interface for differend command using for check messages
 */
@Service
public interface Command {
    void execute(String message);
}
