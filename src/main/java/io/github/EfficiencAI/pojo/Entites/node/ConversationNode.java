package io.github.EfficiencAI.pojo.Entites.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.EfficiencAI.pojo.Entites.PersistentNode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter
public class ConversationNode extends PersistentNode {
    public ConversationNode() {
        LinkedConversationNodesID = new HashSet<>();
    }
    @Override @JsonIgnore
    public String getIdentifier() {
        return ConversationNodeID;
    }
    @Setter
    private String ConversationNodeID;
    @Setter
    private String ContextStartIdx;
    @Setter
    private String ContextEndIdx;
    @Setter
    private String UserMessage;
    @Setter
    private String AIMessage;

    private HashSet<ConversationNode> LinkedConversationNodesID = null;

}
