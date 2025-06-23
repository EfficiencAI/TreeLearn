package io.github.EfficiencAI.pojo.Entites.node;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.EfficiencAI.pojo.Entites.PersistentNode;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = false)
public class UserNode extends PersistentNode {
    private String UserId;
    private String UserName;
    private String SessionsStorageFolderPath;

    @Override @JsonIgnore
    public String getIdentifier() {
        return UserId;
    }

}
