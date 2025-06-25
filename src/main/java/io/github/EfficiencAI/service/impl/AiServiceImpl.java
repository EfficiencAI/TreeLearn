package io.github.EfficiencAI.service.impl;

import io.github.EfficiencAI.pojo.DTO.ChatRequestDTO;
import io.github.EfficiencAI.service.AiService;
import io.github.EfficiencAI.utils.ClientUtil;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class AiServiceImpl implements AiService {

    @Override
    public Flux<String> chat(ChatRequestDTO chatRequestDTO) {
        return ClientUtil.client(chatRequestDTO)
                .chat(chatRequestDTO.getMessage())
                .doOnNext(token -> {
                    System.out.print(token);
                    System.out.flush(); // 确保立即输出到控制台
                })
                .doOnComplete(System.out::println)
                .doOnError(error -> {
                    System.err.println("\n流式输出出错: " + error.getMessage());
                });
    }


}