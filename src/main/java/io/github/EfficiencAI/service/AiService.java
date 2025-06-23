package io.github.EfficiencAI.service;

import io.github.EfficiencAI.pojo.DTO.ChatRequestDTO;
import reactor.core.publisher.Flux;

public interface AiService {
    Flux<String> chat(ChatRequestDTO chatRequestDTO);
}
