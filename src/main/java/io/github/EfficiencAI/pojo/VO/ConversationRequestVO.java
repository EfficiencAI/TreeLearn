package io.github.EfficiencAI.pojo.VO;

import io.github.EfficiencAI.pojo.DTO.ChatRequestDTO;
import io.github.EfficiencAI.pojo.DTO.NodeRequestDTO;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class ConversationRequestVO {
    private String userId;
    private String sessionName;
    private String conversationNodeId;
    private String registrationCertificate;
    private String parentId;
    private String contextStartIdx;
    private String contextEndIdx;

    private String message;
    private String apikey;
    private String baseurl;
    private String modelName;
    private String systemPrompt;
    private String[] mcpUrls;
    
    public NodeRequestDTO toNodeRequestDTO() {
        return NodeRequestDTO
                .builder()
                .userId(userId)
                .sessionName(sessionName)
                .conversationNodeId(conversationNodeId)
                .registrationCertificate(registrationCertificate)
                .userMessage(message)
                .parentId(parentId)
                .contextStartIdx(contextStartIdx)
                .contextEndIdx(contextEndIdx)
                .build();
    }
    
    public ChatRequestDTO toChatRequestDTO() {
        return ChatRequestDTO
                .builder()
                .apikey(apikey)
                .baseurl(baseurl)
                .modelName(modelName)
                .message(message)
                .systemPrompt(systemPrompt)
                .mcpUrls(mcpUrls)
                .build();
    }
}