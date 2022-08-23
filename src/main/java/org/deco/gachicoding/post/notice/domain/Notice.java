package org.deco.gachicoding.post.notice.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import org.deco.gachicoding.common.BaseTimeEntity;
import org.deco.gachicoding.post.notice.domain.vo.NoticeContents;
import org.deco.gachicoding.post.notice.domain.vo.NoticeTitle;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.exception.ApplicationException;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

import static org.deco.gachicoding.exception.StatusEnum.*;

@Getter
@DynamicInsert
@DynamicUpdate
@Entity
public class Notice extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long notIdx;

    @Embedded
    private NoticeTitle notTitle;

    @Embedded
    private NoticeContents notContents;

    private Long notViews;

    private Boolean notPin;

    private Boolean notLocked;

    // FetchType.EAGER 즉시 로딩
    // 1. 대부분의 JPA 구현체는 가능하면 조인을 사용해서 SQL 한번에 함께 조회하려고 한다.
    // 2. 이렇게 하면, 실제 조회할 때 한방 쿼리로 다 조회해온다.
    // FetchType.LAZY 지연 로딩
    // 1. 로딩되는 시점에 Lazy 로딩 설정이 되어있는 Team 엔티티는 프록시 객체로 가져온다.
    // 2. 후에 실제 객체를 사용하는 시점에 초기화가 된다. DB에 쿼리가 나간다.
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_idx")
    @JsonManagedReference
    private User author;

    public static Builder builder() {
        return new Builder();
    }

    protected Notice() {}

    public Notice(Long notIdx, User author, NoticeTitle notTitle, NoticeContents notContents, Long notViews, Boolean notPin, Boolean notLocked) {
        this.notIdx = notIdx;
        this.author = author;
        this.notTitle = notTitle;
        this.notContents = notContents;
        this.notViews = notViews;
        this.notPin = notPin;
        this.notLocked = notLocked;
    }

    public String getAuthorNick() {
        return author.getUserNick();
    }

    public String getAuthorEmail() {
        return author.getUserEmail();
    }

    public String getNotContents() {
        return notContents.getNoticeContents();
    }

    public String getNotTitle() {
        return notTitle.getNoticeTitle();
    }

    public void hasSameAuthor(User user) {
        if (author != user) {
            throw new ApplicationException(INVALID_AUTH_USER);
        }
    }

    public void enableNotice() {
        if (this.notLocked)
            throw new ApplicationException(ALREADY_ACTIVE);
        this.notLocked = true;
    }

    public void disableNotice() {
        if (!this.notLocked)
            throw new ApplicationException(ALREADY_INACTIVE);
        this.notLocked = false;
    }

    public void update(String notTitle, String notContents) {
        updateTitle(notTitle);
        updateContent(notContents);
    }

    public void updateTitle(String notTitle) {
        this.notTitle = new NoticeTitle(notTitle);
    }

    public void updateContent(String notContents) {
        this.notContents = new NoticeContents(notContents);
    }

    public static class Builder {

        private Long notIdx;
        private User writer;
        private NoticeTitle notTitle;
        private NoticeContents notContents;
        private Long notViews;
        private Boolean notPin;
        private Boolean notLocked;

        public Builder notIdx(Long notIdx) {
            this.notIdx = notIdx;
            return this;
        }

        public Builder author(User user) {
            this.writer = user;
            return this;
        }

        public Builder notTitle(String notTitle) {
            this.notTitle = new NoticeTitle(notTitle);
            return this;
        }

        public Builder notContents(String notContents) {
            this.notContents = new NoticeContents(notContents);
            return this;
        }

        public Builder notViews(Long notViews) {
            this.notViews = notViews;
            return this;
        }

        public Builder notPin(Boolean notPin) {
            this.notPin = notPin;
            return this;
        }

        public Builder notLocked(Boolean notLocked) {
            this.notLocked = notLocked;
            return this;
        }

        public Notice build() {
            Notice notice = new Notice(
                    notIdx,
                    writer,
                    notTitle,
                    notContents,
                    notViews,
                    notPin,
                    notLocked
                    );

            return notice;
        }
    }
}