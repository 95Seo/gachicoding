package org.deco.gachicoding.user.dto.request;


import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.user.domain.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class UserSaveRequestDto {

    @ApiModelProperty(value = "사용자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "1234@1234.com")
    @NotNull @Email(message = "올바른 형식의 아이디가 아닙니다.")
    private String userEmail;

    @ApiModelProperty(value = "사용자 이름", required = true, example = "김인환")
    @NotNull
    private String userName;

    @ApiModelProperty(value = "사용자 별명", required = true, example = "비밀번호:1234")
    @NotNull
    private String userNick;

    @ApiModelProperty(value = "비밀번호", required = true, example = "1234")
    @NotNull
    private String userPassword;

    @Builder
    public UserSaveRequestDto(String userName, String userEmail, String userPassword, String userNick) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userNick = userNick;
    }

    public User toEntity() {
        return User.builder()
                .userName(userName)
                .userNick(userNick)
                .userEmail(userEmail)
                .userPassword(userPassword)
                .build();
    }
}
