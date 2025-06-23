package io.github.EfficiencAI.pojo.VO;

import lombok.Data;

@Data
public class NodeRequestVO {
    private String parentId;
    private String userMessage;
    private String contextStartIdx;
    private String contextEndIdx;
}
