package io.github.EfficiencAI.pojo.VO;

import io.github.EfficiencAI.pojo.DTO.UserDTO;
import lombok.Data;

@Data
public class UserVO {
    private String username;
    private String userId;

    public UserDTO toDTO(){
        return UserDTO.builder()
                .userId(userId)
                .username(username)
                .build();
    }
}
