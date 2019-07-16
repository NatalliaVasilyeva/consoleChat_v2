package by.touchsoft.vasilyevanatali.webClient;

import by.touchsoft.vasilyevanatali.Model.ChatMessage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<ChatMessage> {
    /**

     * Gson instance

     */

    private Gson json = new GsonBuilder().create();



    @Override

    public String encode(ChatMessage message) {

        return json.toJson(message);

    }



    @Override

    public void init(EndpointConfig endpointConfig) {



    }



    @Override

    public void destroy() {



    }
}
