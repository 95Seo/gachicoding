package org.deco.gachicoding.post.question.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.tag.dto.TagResponse;
import org.deco.gachicoding.tag.dto.response.TagResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionListResponseDto implements TagResponse {

    private Long queIdx;
    private String userEmail;
    private String userNick;
    private String queTitle;
    private String queContent;
    private String queError;
    private String queCategory;
    private Boolean queSolve;
    private Boolean queActivated;
    private LocalDateTime queRegdate;

    private List<TagResponseDto> tags;

    @Builder
    public QuestionListResponseDto(Question question) {
        this.userEmail = question.getWriter().getUserEmail();
        this.userNick = question.getWriter().getUserNick();
        this.queIdx = question.getQueIdx();
        this.queTitle = question.getQueTitle();
        this.queContent = question.getQueContents();
        this.queError = question.getQueError();
        this.queCategory = question.getQueCategory();
        this.queSolve = question.getQueSolve();
        this.queActivated = question.getQueActivated();
        this.queRegdate = question.getQueRegdate();
    }

    @Override
    public void setTags(List<TagResponseDto> tags) {
        this.tags = tags;
    }

//    public void setWriterInfo(Question question) {
//        User writer = question.getWriter().getUserIdx();
//        this.userIdx = writer.getUserIdx();
//    }
}
