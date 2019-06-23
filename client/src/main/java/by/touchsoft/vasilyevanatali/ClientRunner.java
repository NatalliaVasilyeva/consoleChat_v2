package by.touchsoft.vasilyevanatali;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * @author Natali
 * Start applicatian
 */

public class ClientRunner {
    /**
     * LOGGER variable to log start class Client.
     */
    private static final Logger LOGGER = LogManager.getLogger(ClientRunner.class);

    public static void main(String[] args) {

        LOGGER.info("Client is running");

        /**
         * Start application
         */
        new Client();
    }
}
