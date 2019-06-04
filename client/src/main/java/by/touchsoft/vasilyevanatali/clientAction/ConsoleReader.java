package by.touchsoft.vasilyevanatali.clientAction;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleReader {
    private static final Logger LOGGER = LogManager.getLogger(ConsoleReader.class);
    private BufferedReader consoleBufferedReader;

    public String readFromConsole() {
        try {
            if (consoleBufferedReader == null) {
                consoleBufferedReader = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
            }

            return consoleBufferedReader.readLine().trim();
        } catch (IOException e) {
            LOGGER.debug("Problem with reade from console", e.getMessage());
            return "";
        }

    }

    public void destroy() {
        try {
            if (consoleBufferedReader != null) {
                consoleBufferedReader.close();
            }
        } catch (IOException e) {
            LOGGER.debug("Problem with closing consoleBufferedReader", e.getMessage());
        }

    }
}

