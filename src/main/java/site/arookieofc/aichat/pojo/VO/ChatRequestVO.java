package site.arookieofc.aichat.pojo.VO;

import lombok.Data;
import site.arookieofc.aichat.pojo.DTO.ChatRequestDTO;

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
