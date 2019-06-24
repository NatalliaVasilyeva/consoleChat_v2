package by.touchsoft.vasilyevanatali.clientAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
/**
 * @author Natali
 * Contain method, what help to read from console, destroy stream, what read from console
 */

public class ConsoleReader {
    /**
     * LOGGER variable to log client information.
     */
    private static final Logger LOGGER = LogManager.getLogger(ConsoleReader.class);

    /**
     * variable what help to read from console
     */
    private BufferedReader consoleBufferedReader;

    /**
     * @return String, what user write into console
     */
    public String readFromConsole() {
        try {
            if (consoleBufferedReader == null) {
                consoleBufferedReader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
            }
            return consoleBufferedReader.readLine().trim();
        } catch (IOException e) {
            LOGGER.warn("Problem with reade from console " + e.getMessage());
            return "";
        }
    }


    /**
     * method close stream, what read from console when user disconnect from server
     */
    public void destroy() {
        try {
            if (consoleBufferedReader != null) {
                consoleBufferedReader.close();
            }
        } catch (IOException e) {
            LOGGER.warn("Problem with closing consoleBufferedReader " + e.getMessage());
            System.exit(0);
        }
    }
}

