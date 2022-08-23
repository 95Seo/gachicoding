package org.deco.gachicoding.post.question.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
public class QuestionUpdateRequestDto {

    @NotNull
    @Email(message = "올바른 형식의 아이디가 아닙니다.")
    @ApiModelProperty(value = "요청자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull
    @ApiModelProperty(name = "질문 번호",required = true, example = "1")
    private Long queIdx;

    @ApiModelProperty(name = "질문 제목",required = false, example = "수정된 제목")
    private String queTitle;

    @ApiModelProperty(name = "질문 내용",required = false, example = "수정된 내용")
    private String queContent;

    @ApiModelProperty(name = "질문 에러메시지",required = false, example = "수정된 에러메시지")
    private String queError;

    @ApiModelProperty(name = "질문 카테고리",required = false, example = "수정된 카테고리")
    private String queCategory;

    @Builder
    public QuestionUpdateRequestDto(String userEmail, Long queIdx, String queTitle, String queContent, String queError, String queCategory) {
        this.userEmail = userEmail;
        this.queIdx = queIdx;
        this.queTitle = queTitle;
        this.queContent = queContent;
        this.queError = queError;
        this.queCategory = queCategory;
    }
}
