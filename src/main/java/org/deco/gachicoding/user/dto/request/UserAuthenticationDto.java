package org.deco.gachicoding.user.dto.request;


import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.Collection;

public class UserAuthenticationDto implements UserDetails {

    private String userEmail;
    private String password;

    public UserAuthenticationDto(@JsonProperty("userEmail") String userEmail,@JsonProperty("password") String password) {
        this.userEmail = userEmail;
        this.password = password;
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
                '}';
    }
}
