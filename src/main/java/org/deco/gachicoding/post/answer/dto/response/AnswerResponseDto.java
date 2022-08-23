package org.deco.gachicoding.post.answer.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.question.domain.Question;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class AnswerResponseDto {
    private Long ansIdx;
    private String userEmail;
    private String userNick;
    private Long queIdx;
    private String ansContent;
    private Boolean ansSelect;
    private Boolean ansActivated;
    private LocalDateTime ansRegdate;

    @Builder
    public AnswerResponseDto(Answer answer) {
//        setWriterInfo(answer);
        this.userEmail = answer.getWriter().getUserEmail();
        this.userNick = answer.getWriter().getUserNick();
        setQuestionInfo(answer);
        this.ansIdx = answer.getAnsIdx();
        this.ansContent = answer.getAnsContents();
        this.ansSelect = answer.getAnsSelect();
        this.ansActivated = answer.getAnsActivated();
        this.ansRegdate = answer.getAnsRegdate();
    }

    private void setQuestionInfo(Answer answer) {
        Question question = answer.getQuestion();
        this.queIdx = question.getQueIdx();
    }

//    private void setWriterInfo(Answer answer) {
//        User user = answer.getWriter();
//        this.writerIdx = user.getUserIdx();
//    }

}
