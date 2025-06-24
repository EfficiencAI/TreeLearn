package io.github.EfficiencAI.pojo.Entites.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.EfficiencAI.pojo.Entites.node.Base.PersistentNode;
import lombok.Getter;
import lombok.Setter;
import java.util.HashSet;
import java.util.concurrent.ConcurrentHashMap;

@Getter
public class SessionNode extends PersistentNode {
    public SessionNode() {
        this.allConversationNodes = new ConcurrentHashMap<>();
        this.allConversationNodesID = new HashSet<>();
        this.linkedConversationNodesID = new HashSet<>();
    }
    public SessionNode(String sessionName, String nodesStorageFolderPath) {
        this.SessionName = sessionName;
        this.NodesStorageFolderPath = nodesStorageFolderPath;
        this.allConversationNodes = new ConcurrentHashMap<>();
        this.allConversationNodesID = new HashSet<>();
        this.linkedConversationNodesID = new HashSet<>();
    }


    @Override @JsonIgnore
    public String getIdentifier() {
        return SessionName;
    }
    @Override @JsonIgnore
    protected boolean cascadeDelete(){
        boolean ifAllDeleteOperationExecuteSucceed = true;
        for (String ConversationNodeID : allConversationNodes.keySet()) {
            ConversationNode conversationNode = ConversationNode.loadFromFile(NodesStorageFolderPath + ConversationNodeID + ".json", ConversationNode.class);
            if(conversationNode == null) {
                ifAllDeleteOperationExecuteSucceed = false;
                continue;
            }
            if(!conversationNode.deleteSelfFromFile(NodesStorageFolderPath)) {
                ifAllDeleteOperationExecuteSucceed = false;
            }
        }
        return ifAllDeleteOperationExecuteSucceed;
    }
    @Override @JsonIgnore
    protected boolean cascadeLoad(){
        for (String ConversationNodeID : allConversationNodesID) {
            ConversationNode conversationNode = ConversationNode.loadFromFile(NodesStorageFolderPath + ConversationNodeID + ".json", ConversationNode.class);
            if(conversationNode == null) {
                return false;
            }
            allConversationNodes.put(ConversationNodeID, conversationNode);
        }
        return true;
    }
    @Setter @JsonProperty("SessionName")
    private String SessionName;
    @Setter @JsonProperty("NodesStorageFolderPath")
    private String NodesStorageFolderPath;
    @JsonIgnore
    private final ConcurrentHashMap<String, ConversationNode> allConversationNodes;
    @JsonProperty("AllConversationNodesID")
    private final HashSet<String> allConversationNodesID;
    @JsonProperty("LinkedConversationNodesID")
    private final HashSet<String> linkedConversationNodesID;
}
