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
        AllConversationNodes = new ConcurrentHashMap<>();
        LinkedConversationNodesID = new HashSet<>();
    }
    public SessionNode(String sessionName, String nodesStorageFolderPath) {
        SessionName = sessionName;
        NodesStorageFolderPath = nodesStorageFolderPath;
        AllConversationNodes = new ConcurrentHashMap<>();
        LinkedConversationNodesID = new HashSet<>();
    }


    @Override @JsonIgnore
    public String getIdentifier() {
        return SessionName;
    }
    @Override @JsonIgnore
    protected boolean cascadeDelete(){
        boolean ifAllDeleteOperationExecuteSucceed = true;
        for (String ConversationNodeID : AllConversationNodes.keySet()) {
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
    @Setter @JsonProperty("SessionName")
    private String SessionName;
    @Setter @JsonProperty("NodesStorageFolderPath")
    private String NodesStorageFolderPath;
    @JsonProperty("AllConversationNodes")
    private final ConcurrentHashMap<String, ConversationNode> AllConversationNodes;
    @JsonProperty("LinkedConversationNodesID")
    private final HashSet<String> LinkedConversationNodesID;
}
