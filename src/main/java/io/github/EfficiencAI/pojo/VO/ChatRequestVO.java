package io.github.EfficiencAI.pojo.VO;

import io.github.EfficiencAI.pojo.DTO.ChatRequestDTO;
import lombok.Data;

@Data
public class ChatRequestVO {
    private String message;
    private String apikey;
    private String baseurl;
    private String modelName;
    private String userId;
    private String systemPrompt;

    public ChatRequestDTO toChatRequestDTO() {
        return ChatRequestDTO
                .builder()
                .apikey(apikey)
                .baseurl(baseurl)
                .modelName(modelName)
                .message(message)
                .systemPrompt(systemPrompt)
                .build();
    }
}
