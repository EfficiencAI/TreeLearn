package io.github.EfficiencAI.Entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;

public class ConversationNode extends PersistentNode {
    public ConversationNode() {
        LinkedConversationNodesID = new HashSet<>();
    }
    @Override @JsonIgnore
    public String getIdentifier() {
        return ConversationNodeID;
    }
    @JsonProperty("ConversationNodeID")
    private String ConversationNodeID;
    @JsonProperty("ContextStartIdx")
    private String ContextStartIdx;
    @JsonProperty("ContextEndIdx")
    private String ContextEndIdx;
    @JsonProperty("UserMessage")
    private String UserMessage;
    @JsonProperty("AIMessage")
    private String AIMessage;
    @JsonProperty("LinkedConversationNodesID")
    private HashSet<ConversationNode> LinkedConversationNodesID = null;



    // Getters And Setters
    public String getConversationNodeID() {
        return ConversationNodeID;
    }
    public void setConversationNodeID(String conversationNodeID) {
        ConversationNodeID = conversationNodeID;
    }
    public HashSet<ConversationNode> getLinkedConversationNodesID() {
        return LinkedConversationNodesID;
    }
    public String getAIMessage() {
        return AIMessage;
    }
    public void setAIMessage(String AIMessage) {
        this.AIMessage = AIMessage;
    }
    public String getUserMessage() {
        return UserMessage;
    }
    public void setUserMessage(String userMessage) {
        UserMessage = userMessage;
    }
    public String getContextEndIdx() {
        return ContextEndIdx;
    }
    public void setContextEndIdx(String contextEndIdx) {
        ContextEndIdx = contextEndIdx;
    }
    public String getContextStartIdx() {
        return ContextStartIdx;
    }
    public void setContextStartIdx(String contextStartIdx) {
        ContextStartIdx = contextStartIdx;
    }
}
