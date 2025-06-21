package site.arookieofc.aichat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import site.arookieofc.aichat.pojo.VO.ChatRequestVO;
import site.arookieofc.aichat.service.AiService;

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
