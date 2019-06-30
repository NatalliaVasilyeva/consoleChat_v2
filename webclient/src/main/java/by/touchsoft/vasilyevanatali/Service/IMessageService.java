package by.touchsoft.vasilyevanatali.Service;

import by.touchsoft.vasilyevanatali.Message.ChatMessage;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

public interface IMessageService {

     String convertToJson(ChatMessage message) throws JsonProcessingException;

     ChatMessage parseFromJson(String json) throws IOException;
}
