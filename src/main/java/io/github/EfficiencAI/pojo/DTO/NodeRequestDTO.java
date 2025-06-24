package io.github.EfficiencAI.pojo.DTO;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NodeRequestDTO {
    private String userId;
    private String sessionName;
    private String conversationNodeId;
    private String parentId;
    private String userMessage;
    private String contextStartIdx;
    private String contextEndIdx;
}
