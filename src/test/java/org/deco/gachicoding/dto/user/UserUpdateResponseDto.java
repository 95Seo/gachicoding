package org.deco.gachicoding.dto.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.user.UserRole;

@Setter
@Getter
@NoArgsConstructor
public class UserUpdateResponseDto {

    private String name;
    private String email;
    private String password;
    private int activated;
    private UserRole role;

    public UserUpdateResponseDto(String name, String email, String password, int activated, UserRole role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.activated = activated;
        this.role = role;
    }
}
