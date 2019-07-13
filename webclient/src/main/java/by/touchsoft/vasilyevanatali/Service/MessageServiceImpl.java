package by.touchsoft.vasilyevanatali.Service;

import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.IOException;

/**
 * @author Natali
 * Message service class
 */

@Service
public enum MessageServiceImpl implements IMessageService {

    INSTANCE {
        /**
         * Method convert chat message to json format
         * @param message - chat message
         * @return String in jsonFormat
         * @throws JsonProcessingException
         */

        @Override
        public String convertToJson(ChatMessage message) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return objectMapper.writeValueAsString(message);
        }


        /**
         * Method convert json string to chat message
         * @param json - string in json format
         * @return - object of ChatMessage
         * @throws IOException
         */

        @Override
        public ChatMessage parseFromJson(String json) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            return objectMapper.readValue(json, ChatMessage.class);
        }

    }

}
