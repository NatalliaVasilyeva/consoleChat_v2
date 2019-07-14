package by.touchsoft.vasilyevanatali.WebClient;

import by.touchsoft.vasilyevanatali.Model.ChatMessage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import javax.websocket.Decoder;
import javax.websocket.EndpointConfig;


public class MessageDecoder implements Decoder.Text<ChatMessage>{

        private Gson json = new GsonBuilder().create();



        @Override

        public ChatMessage decode(String s) {

            return json.fromJson(s, ChatMessage.class);

        }



        @Override

        public boolean willDecode(String s) {

            return s != null && !s.isEmpty();

        }



        @Override

        public void init(EndpointConfig endpointConfig) {



        }



        @Override

        public void destroy() {



        }
}
