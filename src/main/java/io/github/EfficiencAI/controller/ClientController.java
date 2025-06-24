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

    @PostMapping("/chat")
    public Flux<String> chat(@RequestBody ChatRequestVO chatRequestVO) {
        return aiService.chat(chatRequestVO.toDTO());
    }
}