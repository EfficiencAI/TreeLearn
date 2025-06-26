package io.github.EfficiencAI.pojo.VO;

import io.github.EfficiencAI.pojo.DTO.ChatRequestDTO;
import lombok.Data;

@Data
public class ChatRequestVO {
    private String message;
    private String apikey;
    private String baseurl;
    private String modelName;
    private String[] mcpUrls;
    private String userId;
    private String sessionName;
    private String systemPrompt;

    public ChatRequestDTO toDTO() {
        return ChatRequestDTO
                .builder()
                .mcpUrls(mcpUrls)
                .apikey(apikey)
                .baseurl(baseurl)
                .modelName(modelName)
                .message(message)
                .systemPrompt(systemPrompt)
                .build();
    }
}
