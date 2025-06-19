package site.arookieofc.aichat.configuration.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class Client {

    @Value("${spring.ai.system.prompt}")
    private static String SYSTEM_PROMPT;

    @Bean
    public ChatClient chatClient(OllamaChatModel ollamaChatModel) {
        return ChatClient
                .builder(ollamaChatModel)
                .defaultSystem(SYSTEM_PROMPT)
                .build();
    }
}
