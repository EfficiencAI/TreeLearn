package io.github.EfficiencAI.pojo.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ChatRequestDTO {
    private String systemPrompt;
    private String apikey;
    private String modelName;
    private String baseurl;
    private String message;
    private String[] mcpUrls;
}
