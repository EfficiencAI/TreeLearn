package io.github.EfficiencAI.controller;

import io.github.EfficiencAI.pojo.VO.ChatRequestVO;
import io.github.EfficiencAI.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping(value = "/ai")
public class ClientController {

    @Autowired
    private AiService aiService;

    @GetMapping("/chat")
    public Flux<String> chat(ChatRequestVO chatRequestVO) {
        return aiService.chat(chatRequestVO.toChatRequestDTO());
    }
}
