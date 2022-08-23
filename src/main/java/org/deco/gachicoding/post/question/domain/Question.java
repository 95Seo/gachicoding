package org.deco.gachicoding.post.question.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.user.domain.User;
import org.deco.gachicoding.post.question.dto.request.QuestionUpdateRequestDto;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicInsert
@DynamicUpdate
@Table(name = "gachi_q")
public class Question {
    @Id
    @Column(name = "qs_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long queIdx;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_idx")
    @JsonManagedReference
    private User writer;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "qs_idx", insertable = false, updatable = false)
    @JsonBackReference
    private List<Answer> answers = new ArrayList<>();

    @Column(name = "qs_title")
    private String queTitle;

    @Column(name = "qs_contents")
    private String queContents;

    @Column(name = "qs_error")
    private String queError;

    @Column(name = "qs_category")
    private String queCategory;

    @Column(name = "qs_solve")
    private Boolean queSolve;

    @Column(name = "qs_activated")
    private Boolean queActivated;

    @Column(name = "qs_regdate")
    private LocalDateTime queRegdate;

    @Builder
    public Question(User writer, Long queIdx, String queTitle, String queContents, String queError, String queCategory, Boolean queSolve, Boolean queActivated, LocalDateTime queRegdate) {
        this.writer = writer;
        this.queIdx = queIdx;
        this.queTitle = queTitle;
        this.queContents = queContents;
        this.queError = queError;
        this.queCategory = queCategory;
        this.queSolve = queSolve;
        this.queActivated = queActivated;
        this.queRegdate = queRegdate;
    }

    public void setUser(User user) {
        this.writer = user;
    }

    public void setAnswers(Answer answer) {
        this.answers.add(answer);
    }

    public Question update(QuestionUpdateRequestDto dto) {
        this.queTitle = dto.getQueTitle();
        this.queContents = dto.getQueContent();
        this.queError = dto.getQueError();
        this.queCategory = dto.getQueCategory();
        return this;
    }

    public Question toSolve() {
        this.queSolve = true;
        return this;
    }

    public Question isDisable() {
        this.queActivated = false;
        return this;
    }

    public Question isEnable() {
        this.queActivated = true;
        return this;
    }

    public Question updateContent(String queContent) {
        this.queContents = queContent;
        return this;
    }

    public Question updateError(String queError) {
        this.queError = queError;
        return this;
    }

}
