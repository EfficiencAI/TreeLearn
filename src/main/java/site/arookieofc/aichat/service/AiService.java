package site.arookieofc.aichat.service;

import reactor.core.publisher.Flux;

public interface AiService {
    Flux<String> chat(String message);
}
