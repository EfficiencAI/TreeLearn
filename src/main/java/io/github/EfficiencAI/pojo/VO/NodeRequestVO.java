package io.github.EfficiencAI.pojo.VO;

import io.github.EfficiencAI.pojo.DTO.NodeRequestDTO;
import lombok.Data;

@Data
public class NodeRequestVO {
    private String parentId;
    private String userMessage;
    private String contextStartIdx;
    private String contextEndIdx;

    public NodeRequestDTO toDTO(){
        return NodeRequestDTO
                .builder()
                .parentId(parentId)
                .userMessage(userMessage)
                .contextStartIdx(contextStartIdx)
                .contextEndIdx(contextEndIdx)
                .build();
    }
}
