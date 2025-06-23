package io.github.EfficiencAI.pojo.Entites.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.EfficiencAI.pojo.Entites.node.Base.PersistentNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.HashSet;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserNode extends PersistentNode {
    UserNode() {
        SessionNames = new HashSet<>();
    }
    @JsonProperty("UserId")
    private String UserId;
    @JsonProperty("UserName")
    private String UserName;
    @JsonProperty("SessionsStorageFolderPath")
    private String SessionsStorageFolderPath;
    @JsonProperty("SessionNames")
    private HashSet<String> SessionNames;

    @Override @JsonIgnore
    public String getIdentifier() {
        return UserId;
    }
    @Override @JsonIgnore
    protected boolean cascadeDelete() {
        boolean ifAllDeleteOperationExecuteSucceed = true;
        for (String SessionName : SessionNames) {
            SessionNode sessionNode = SessionNode.loadFromFile(SessionsStorageFolderPath + "/" + SessionName, SessionNode.class);
            if (sessionNode == null) {
                ifAllDeleteOperationExecuteSucceed = false;
                continue;
            }
            if(!sessionNode.deleteSelfFromFile(SessionsStorageFolderPath + "/" + SessionName)){
                ifAllDeleteOperationExecuteSucceed = false;
            }
        }
        return ifAllDeleteOperationExecuteSucceed;
    }

}
