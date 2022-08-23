package org.deco.gachicoding.user.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
public class UserResponseDto {

    private Long userIdx;
    private String userName;
    private String userNick;
    private String userEmail;
    private String userPassword;
    private LocalDateTime userCreatedAt;
    private boolean userLocked;
    private boolean userEnabled;

    public UserResponseDto(User user) {
        this.userIdx = user.getUserIdx();
        this.userName = user.getUserName();
        this.userNick = user.getUserNick();
        this.userEmail = user.getUserEmail();
        this.userPassword = user.getUserPassword();
        this.userCreatedAt = user.getUserCreatedAt();
        this.userLocked = user.isUserLocked();
        this.userEnabled = user.isUserEnabled();
    }
}
