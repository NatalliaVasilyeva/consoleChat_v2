package by.touchsoft.vasilyevanatali.Configuration;

import by.touchsoft.vasilyevanatali.WebClient.ChatEndPoint;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author Natali
 * Configure webServer endpoint to has one port for listen web users and rest users
 */

@Configuration
    @EnableWebSocket
    public class WebsocketConfiguration implements WebSocketConfigurer {


    /**
     * Registry webSocket
     * @param registry
     */
        @Override
        public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {

        }

    /**
     * Return new endPoint
     * @return chatEndPoint
     */
    @Bean
        public ChatEndPoint webServerEndpoint() {

            return new ChatEndPoint();
        }

        @Bean
        public ServerEndpointExporter serverEndpointExporter() {
            return new ServerEndpointExporter();
        }
}
