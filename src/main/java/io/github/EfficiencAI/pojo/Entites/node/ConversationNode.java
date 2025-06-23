package io.github.EfficiencAI.pojo.Entites.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.EfficiencAI.pojo.Entites.node.Base.PersistentNode;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;

@Getter
public class ConversationNode extends PersistentNode {
    public ConversationNode() {
        LinkedConversationNodesID = new HashSet<>();
    }
    public ConversationNode(String conversationNodeID, String contextStartIdx, String contextEndIdx, String userMessage, String AIMessage) {
        this.ConversationNodeID = conversationNodeID;
        this.ContextStartIdx = contextStartIdx;
        this.ContextEndIdx = contextEndIdx;
        this.UserMessage = userMessage;
        this.AIMessage = AIMessage;
        this.LinkedConversationNodesID = new HashSet<>();
    }
    @Override @JsonIgnore
    public String getIdentifier() {
        return ConversationNodeID;
    }
    @Override @JsonIgnore
    protected boolean cascadeDelete(){
        /*
            由于ConversationNode不能跳过SessionNode获取到子节点
            因此对话节点的连锁删除逻辑移至DAO中实现
            此处仅返回true
         */
        return true;
    }
    @Setter @JsonProperty("ConversationNodeID")
    private String ConversationNodeID;
    @Setter @JsonProperty("ContextStartIdx")
    private String ContextStartIdx;
    @Setter @JsonProperty("ContextEndIdx")
    private String ContextEndIdx;
    @Setter @JsonProperty("UserMessage")
    private String UserMessage;
    @Setter @JsonProperty("AIMessage")
    private String AIMessage;
    @JsonProperty("LinkedConversationNodesID")
    private HashSet<String> LinkedConversationNodesID = null;

}
