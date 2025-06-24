package io.github.EfficiencAI.service.impl;

import io.github.EfficiencAI.pojo.DTO.ChatRequestDTO;
import io.github.EfficiencAI.service.AiService;
import io.github.EfficiencAI.utils.ClientUtil;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AiServiceImpl implements AiService {

    @Override
    public Flux<String> chat(ChatRequestDTO chatRequestDTO) {
        return ClientUtil.chatClient(chatRequestDTO)
                .prompt(new Prompt(
                        chatRequestDTO.getSystemPrompt() == null
                                ? "你是一个小助手"
                                : chatRequestDTO.getSystemPrompt(),
                        OpenAiChatOptions
                                .builder()
                                .model(chatRequestDTO.getModelName())
                                .build()
                ))
                .user(chatRequestDTO.getMessage())
                .stream()
                .content();
    }
}
