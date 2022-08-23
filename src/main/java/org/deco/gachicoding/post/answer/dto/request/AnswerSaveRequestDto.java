package org.deco.gachicoding.post.answer.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.post.answer.domain.Answer;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.user.domain.User;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AnswerSaveRequestDto {

    @ApiModelProperty(value = "사용자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    @NotNull
    @Email(message = "올바른 형식의 아이디가 아닙니다.")
    private String userEmail;

    @ApiModelProperty(value = "답변할 질문 글 번호", required = true, example = "1")
    @NotNull
    private Long queIdx;

    @ApiModelProperty(value = "답변 내용", required = true, example = "Spring Security 아시나요~~ 얼마나 사랑했는지~~ 그댈 보면 자꾸 눈물이 나서~")
    @NotNull
    private String ansContent;

    @Builder
    public AnswerSaveRequestDto(String userEmail, Long queIdx, String ansContent) {
        this.userEmail = userEmail;
        this.queIdx = queIdx;
        this.ansContent = ansContent;
    }

    public Answer toEntity(User writer, Question question){
        return Answer.builder()
                .writer(writer)
                .question(question)
                .ansContents(ansContent)
                .build();
    }
}
