package io.github.EfficiencAI.Entites;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UserNode extends PersistentNode{
    @JsonProperty("UserId")
    private String UserId;
    @JsonProperty("UserName")
    private String UserName;
    @JsonProperty("SessionsStorageFolderPath")
    private String SessionsStorageFolderPath;

    @Override @JsonIgnore
    public String getIdentifier() {
        return UserId;
    }





    // Getters And Setters
    public String getSessionsStorageFolderPath() {
        return SessionsStorageFolderPath;
    }
    public void setSessionsStorageFolderPath(String sessionsStorageFolderPath) {
        SessionsStorageFolderPath = sessionsStorageFolderPath;
    }
    public String getUserName() {
        return UserName;
    }
    public void setUserName(String userName) {
        UserName = userName;
    }
    public String getUserId() {
        return UserId;
    }
    public void setUserId(String userId) {
        UserId = userId;
    }
}
