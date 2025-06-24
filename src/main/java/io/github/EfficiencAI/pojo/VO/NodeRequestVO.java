package io.github.EfficiencAI.pojo.VO;

import io.github.EfficiencAI.pojo.DTO.NodeRequestDTO;
import lombok.Data;

@Data
public class NodeRequestVO {
    private String userId;
    private String sessionName;
    private String conversationNodeId;
    private String parentId;
    private String userMessage;
    private String contextStartIdx;
    private String contextEndIdx;

    public NodeRequestDTO toDTO(){
        return NodeRequestDTO
                .builder()
                .userId(userId)
                .sessionName(sessionName)
                .conversationNodeId(conversationNodeId)
                .parentId(parentId)
                .userMessage(userMessage)
                .contextStartIdx(contextStartIdx)
                .contextEndIdx(contextEndIdx)
                .build();
    }
}
