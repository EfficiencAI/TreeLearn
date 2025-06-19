package site.arookieofc.aichat.service.impl;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import site.arookieofc.aichat.service.AiService;

@Service
public class AiServiceImpl implements AiService {

    @Autowired
    private ChatClient client;

    @Override
    public Flux<String> chat(String message) {
        return client
                .prompt()
                .user(message)
                .advisors()
                .stream()
                .content();
    }
}
