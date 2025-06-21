package site.arookieofc.aichat.service;

import reactor.core.publisher.Flux;
import site.arookieofc.aichat.pojo.DTO.ChatRequestDTO;

public interface AiService {
    Flux<String> chat(ChatRequestDTO chatRequestDTO);
}
