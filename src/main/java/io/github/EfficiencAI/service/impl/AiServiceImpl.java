package io.github.EfficiencAI.service.impl;

import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.data.message.UserMessage;
import io.github.EfficiencAI.pojo.DTO.ChatRequestDTO;
import io.github.EfficiencAI.service.AiService;
import io.github.EfficiencAI.utils.ClientUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.List;

@Service
public class AiServiceImpl implements AiService {

    @Override
    public Flux<String> chat(ChatRequestDTO chatRequestDTO) {
        List<ChatMessage> list = new ArrayList<>();
        if(chatRequestDTO.getContextAIMessage() == null || chatRequestDTO.getContextUserMessage() == null){
           list.add(UserMessage.userMessage(chatRequestDTO.getMessage()));
        }else {
            list.add(UserMessage.userMessage(chatRequestDTO.getContextUserMessage()));
            list.add(UserMessage.userMessage(chatRequestDTO.getMessage()));
            list.add(AiMessage.aiMessage(chatRequestDTO.getContextAIMessage()));
        }
        return ClientUtil.client(chatRequestDTO)
                .chat(
                       list.toArray(ChatMessage[]::new)
                )
                .doOnNext(token -> {
                    System.out.print(token);
                    System.out.flush();
                })
                .doOnComplete(System.out::println)
                .doOnError(error -> {
                    System.err.println("\n流式输出出错: " + error.getMessage());
                })
                // 添加超时处理，不要返回默认消息
                .onErrorResume(throwable -> {
                    if (throwable.getMessage().contains("timed out")) {
                        return Flux.error(new RuntimeException("AI服务连接超时，请稍后重试"));
                    }
                    return Flux.error(throwable);
                });
    }


}