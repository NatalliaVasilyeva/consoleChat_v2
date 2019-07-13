package by.touchsoft.vasilyevanatali.Service;

import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

/**
 * @author Natali
 * Message service interface
 */

public interface IMessageService {

    /**
     * Method convert chat message to json format
     *
     * @param message - chat message
     * @return String in jsonFormat
     * @throws JsonProcessingException
     */

    String convertToJson(ChatMessage message) throws JsonProcessingException;


    /**
     * Method convert json string to chat message
     *
     * @param json - string in json format
     * @return - object of ChatMessage
     * @throws IOException
     */
    ChatMessage parseFromJson(String json) throws IOException;
}
