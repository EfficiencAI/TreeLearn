package io.github.EfficiencAI.Entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

public class SessionNode extends PersistentNode{
    public SessionNode() {
        AllConversationNodes = new ConcurrentHashMap<>();
        LinkedConversationNodesID = new HashSet<>();
    }
    @Override @JsonIgnore
    public String getIdentifier() {
        return SessionId;
    }
    @JsonProperty("SessionId")
    private String SessionId;
    @JsonProperty("SessionName")
    private String SessionName;
    @JsonProperty("NodesStorageFolderPath")
    private String NodesStorageFolderPath;
    @JsonProperty("AllConversationNodes")
    private ConcurrentHashMap<String, ConversationNode> AllConversationNodes = null;
    @JsonProperty("LinkedConversationNodesID")
    private HashSet<ConversationNode> LinkedConversationNodesID = null;

    // Getters And Setters
    public String getSessionId() {
        return SessionId;
    }
    public void setSessionId(String sessionId) {
        SessionId = sessionId;
    }
    public String getSessionName() {
        return SessionName;
    }
    public void setSessionName(String sessionName) {
        SessionName = sessionName;
    }
    public String getNodesStorageFolderPath() {
        return NodesStorageFolderPath;
    }
    public void setNodesStorageFolderPath(String nodesStorageFolderPath) {
        NodesStorageFolderPath = nodesStorageFolderPath;
    }
    public ConcurrentHashMap<String, ConversationNode> getAllConversationNodes() {
        return AllConversationNodes;
    }
    public HashSet<ConversationNode> getLinkedConversationNodesID() {
        return LinkedConversationNodesID;
    }

}
