package org.deco.gachicoding.user.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class UserUpdateRequestDto {

    @ApiModelProperty(value = "수정할 비밀번호", required = false, example = "12345")
    private String userPassword;

    @ApiModelProperty(value = "수정할 사용자 별명", required = false, example = "비밀번호:12345")
    private String userNick;

    @ApiModelProperty(value = "계정 활성 또는 비활성", required = false, example = "false")
    private boolean userLocked;

    @ApiModelProperty(value = "계정 인증여부", required = false, example = "true")
    private boolean userEnabled;

    public UserUpdateRequestDto(String userNick, String userPassword, boolean userLocked, boolean userEnabled) {
        this.userNick = userNick;
        this.userPassword = userPassword;
        this.userLocked = userLocked;
        this.userEnabled = userEnabled;
    }
}