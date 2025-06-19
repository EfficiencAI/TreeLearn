package site.arookieofc.aichat.controller;

import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import site.arookieofc.aichat.service.AiService;

@RestController
@RequestMapping(value = "/ai")
public class ClientController {

    @GetMapping("/chat")
    public Flux<String> chat(String message, AiService aiService) {
        return aiService.chat(message);
    }

}
