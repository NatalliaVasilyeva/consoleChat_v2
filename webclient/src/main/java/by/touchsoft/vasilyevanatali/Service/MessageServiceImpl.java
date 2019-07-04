package by.touchsoft.vasilyevanatali.Service;

import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public enum MessageServiceImpl implements IMessageService {

    INSTANCE{
        @Override
        public String convertToJson(ChatMessage message) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            return objectMapper.writeValueAsString(message);
        }


        @Override
        public ChatMessage parseFromJson(String json) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();
            return objectMapper.readValue(json, ChatMessage.class);
        }

    }

}
