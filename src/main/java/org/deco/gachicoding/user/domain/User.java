package org.deco.gachicoding.user.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@DynamicInsert
@DynamicUpdate
@Entity
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    private String userName;
    private String userNick;
    private String userEmail;
    private String userPassword;
    private LocalDateTime userCreatedAt;
    private boolean userLocked;
    private boolean userEnabled;
    private String userRole;

    @Builder
    public User(Long userIdx, String userName, String userNick, String userEmail, String userPassword, LocalDateTime userCreatedAt, boolean userLocked, boolean userEnabled) {
        this.userIdx = userIdx;
        this.userName = userName;
        this.userNick = userNick;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userCreatedAt = userCreatedAt;
        this.userLocked = userLocked;
        this.userEnabled = userEnabled;
    }

    public User update(String userNick, String userPassword, boolean userActivated, boolean userEnabled) {
        this.userNick = userNick;
        this.userPassword = userPassword;
        this.userLocked = userActivated;
        this.userEnabled = userEnabled;
        return this;
    }

    public void emailAuthenticated() {
        this.userEnabled = true;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final User user = (User) o;
        return Objects.equals(userIdx, user.getUserIdx());
    }

    @Override
    public int hashCode() {
        return Objects.hash(userIdx);
    }
}