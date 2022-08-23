package org.deco.gachicoding.post.answer.domain;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.user.domain.User;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "gachi_a")
public class Answer {
    @Id
    @Column(name = "as_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ansIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    @JsonManagedReference
    private User writer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "qs_idx")
    @JsonManagedReference
    private Question question;

    @Column(name = "as_contents")
    private String ansContents;

    @Column(name = "as_select")
    private Boolean ansSelect;

    @Column(name = "as_activated")
    private Boolean ansActivated;

    @Column(name = "as_regdate")
    private LocalDateTime ansRegdate;

    @Builder
    public Answer(User writer, Question question, String ansContents, Boolean ansSelect, Boolean ansActivated, LocalDateTime ansRegdate) {
        this.writer = writer;
        this.question = question;
        this.ansContents = ansContents;
        this.ansSelect = ansSelect;
        this.ansActivated = ansActivated;
        this.ansRegdate = ansRegdate;
    }

    public void setUser(User writer) {
        this.writer = writer;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public Answer update(String ansContent) {
        this.ansContents = ansContent;
        return this;
    }

    public Answer toSelect() {
        this.ansSelect = true;
        return this;
    }

    public Answer disableAnswer() {
        this.ansActivated = false;
        return this;
    }

    public Answer enableAnswer() {
        this.ansActivated = true;
        return this;
    }
}