package io.github.EfficiencAI.pojo.Entites.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.EfficiencAI.pojo.Entites.PersistentNode;
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
    @Override @JsonIgnore
    public String getIdentifier() {
        return SessionId;
    }
    @Setter
    private String SessionId;
    @Setter
    private String SessionName;
    @Setter
    private String NodesStorageFolderPath;
    private final ConcurrentHashMap<String, ConversationNode> AllConversationNodes;
    private final HashSet<ConversationNode> LinkedConversationNodesID;
}
