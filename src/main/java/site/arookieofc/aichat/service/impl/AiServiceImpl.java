package site.arookieofc.aichat.service.impl;

import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import site.arookieofc.aichat.pojo.DTO.ChatRequestDTO;
import site.arookieofc.aichat.service.AiService;
import site.arookieofc.aichat.utils.ClientUtil;

@Service
public class AiServiceImpl implements AiService {

    @Override
    public Flux<String> chat(ChatRequestDTO chatRequestDTO) {
        return ClientUtil.openAiChatClient(chatRequestDTO)
                .prompt(new Prompt(
                        chatRequestDTO.getSystemPrompt(),
                        ChatOptions.builder()
                                .model(chatRequestDTO.getModelName())
                                .build()
                ))
                .user(chatRequestDTO.getMessage())
                .advisors()
                .stream()
                .content();
    }
}
