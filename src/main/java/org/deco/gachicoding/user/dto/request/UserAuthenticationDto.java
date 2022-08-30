package org.deco.gachicoding.user.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.SpringSecurityCoreVersion;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collection;

public class UserAuthenticationDto implements UserDetails {

    private static final long serialVersionUID = SpringSecurityCoreVersion.SERIAL_VERSION_UID;

    private String userEmail;
    private String password;
    private String userNick;

    public UserAuthenticationDto(@JsonProperty("userEmail") String userEmail, @JsonProperty("password") String password) {
        this.userEmail = userEmail;
        this.password = password;
    }

    public UserAuthenticationDto(String userEmail, String password, String userNick) {
        this.userEmail = userEmail;
        this.password = password;
        this.userNick = userNick;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.userEmail;
    }

    public String getUserNick() {
        return userNick;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String toString() {
        return "UserAuthenticationDto{" +
                "userEmail='" + userEmail + '\'' +
                ", password='" + password + '\'' +
                ", userNick='" + userNick + '\'' +
                '}';
    }
}
