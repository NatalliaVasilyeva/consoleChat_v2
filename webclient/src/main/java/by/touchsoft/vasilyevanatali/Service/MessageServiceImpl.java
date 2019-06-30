package by.touchsoft.vasilyevanatali.Service;

import by.touchsoft.vasilyevanatali.Message.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;


public enum MessageServiceImpl implements IMessageService {

    INSTANCE{
        @Override
        public String convertToJson(ChatMessage message) throws JsonProcessingException {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.registerModule(new JavaTimeModule());
            objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            String res = objectMapper.writeValueAsString(message);
            System.out.println("JSON string: " + res);
            return res;
        }


        @Override
        public ChatMessage parseFromJson(String json) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.findAndRegisterModules();

            ChatMessage res = objectMapper.readValue(json, ChatMessage.class);
            System.out.println("Restored string : " + res);
            return res;
        }

    }

}
