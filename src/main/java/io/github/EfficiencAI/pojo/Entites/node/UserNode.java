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
    public UserNode() {
        sessionNames = new HashSet<>();
    }
    public UserNode(String UserId, String UserName, String SessionsStorageFolderPath) {
        this.UserId = UserId;
        this.UserName = UserName;
        this.SessionsStorageFolderPath = SessionsStorageFolderPath;
        this.sessionNames = new HashSet<>();
    }
    @JsonProperty("UserId")
    private String UserId;
    @JsonProperty("UserName")
    private String UserName;
    @JsonProperty("SessionsStorageFolderPath")
    private String SessionsStorageFolderPath;
    @JsonProperty("SessionNames")
    private HashSet<String> sessionNames;

    @Override @JsonIgnore
    public String getIdentifier() {
        return UserId;
    }
    @Override @JsonIgnore
    protected boolean cascadeDelete() {
        boolean ifAllDeleteOperationExecuteSucceed = true;
        for (String SessionName : sessionNames) {
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
