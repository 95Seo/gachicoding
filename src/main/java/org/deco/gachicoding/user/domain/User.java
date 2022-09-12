package org.deco.gachicoding.user.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@DynamicInsert
@DynamicUpdate
@EqualsAndHashCode(of = "userIdx")
@NoArgsConstructor
@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userIdx;

    @Column(nullable = false)
    private String userName;

    @Column(nullable = false, unique = true)
    private String userNick;

    @Column(nullable = false, unique = true)
    private String userEmail;

    @Column(nullable = false)
    private String userPassword;

    @Column(nullable = false)
    @ColumnDefault("true")
    private boolean userLocked;

    @Column(nullable = false)
    @ColumnDefault("false")
    private boolean userEnabled;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime userCreatedAt;

    @Column(nullable = false)
    @ColumnDefault("\'ROLE_USER\'")
    @Enumerated(EnumType.STRING)
    private RoleType userRole;

    @Builder
    public User(Long userIdx, String userName, String userNick, String userEmail, String userPassword, boolean userLocked, boolean userEnabled) {
        this.userIdx = userIdx;
        this.userName = userName;
        this.userNick = userNick;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userLocked = userLocked;
        this.userEnabled = userEnabled;
    }

    public User(Long userIdx, String userName, String userNick, String userEmail, String userPassword, boolean userLocked, boolean userEnabled, LocalDateTime userCreatedAt, RoleType userRole) {
        this.userIdx = userIdx;
        this.userName = userName;
        this.userNick = userNick;
        this.userEmail = userEmail;
        this.userPassword = userPassword;
        this.userLocked = userLocked;
        this.userEnabled = userEnabled;
        this.userCreatedAt = userCreatedAt;
        this.userRole = userRole;
    }

    public User update(String userNick, boolean userActivated, boolean userEnabled) {
        this.userNick = userNick;
        this.userLocked = userActivated;
        this.userEnabled = userEnabled;
        return this;
    }

    public void isEmailAuthenticated() {
        this.userEnabled = true;
    }

    public void changeNewPassword(String password) {
        this.userPassword = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "userIdx=" + userIdx +
                ", userName='" + userName + '\'' +
                ", userNick='" + userNick + '\'' +
                ", userEmail='" + userEmail + '\'' +
                ", userPassword='" + userPassword + '\'' +
                ", userLocked=" + userLocked +
                ", userEnabled=" + userEnabled +
                ", userCreatedAt=" + userCreatedAt +
                ", userRole='" + userRole + '\'' +
                '}';
    }
}