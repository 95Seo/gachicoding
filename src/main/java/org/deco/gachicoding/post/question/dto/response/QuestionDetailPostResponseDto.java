package org.deco.gachicoding.post.question.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.post.PostResponseDto;
import org.deco.gachicoding.post.answer.dto.response.AnswerResponseDto;
import org.deco.gachicoding.file.dto.response.FileResponseDto;
import org.deco.gachicoding.tag.dto.response.TagResponseDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionDetailPostResponseDto implements PostResponseDto {

    private Long queIdx;
    private String userEmail;
    private String userNick;
    private List<AnswerResponseDto> answerList = new ArrayList<>();
    private String queTitle;
    private String queContent;
    private String queError;
    private String queCategory;
    private Boolean queSolve;
    private Boolean queActivated;
    private LocalDateTime queRegdate;

    private List<FileResponseDto> files;
    private List<TagResponseDto> tags;

    @Builder
    public QuestionDetailPostResponseDto(Question question) {
        this.queIdx = question.getQueIdx();
        this.userEmail = question.getWriter().getUserEmail();
        this.userNick = question.getWriter().getUserNick();
        setAnswerList(question);

        this.queTitle = question.getQueTitle();
        this.queContent = question.getQueContents();
        this.queError = question.getQueError();
        this.queCategory = question.getQueCategory();
        this.queSolve = question.getQueSolve();
        this.queActivated = question.getQueActivated();
        this.queRegdate = question.getQueRegdate();
    }

//    public void setWriterInfo(Question question) {
//        User user = question.getUser();
//        this.userIdx = user.getUserIdx();
//    }

    public void setAnswerList(Question question) {
        for(Answer ans : question.getAnswers()) {
            AnswerResponseDto answerResponseDto = AnswerResponseDto.builder()
                    .answer(ans).build();
            answerList.add(answerResponseDto);
        }
    }

    @Override
    public void setFiles(List<FileResponseDto> files) {
        this.files = files;
    }

    @Override
    public void setTags(List<TagResponseDto> tags) {
        this.tags = tags;
    }
}
