package org.deco.gachicoding.post.question.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.post.question.domain.Question;
import org.deco.gachicoding.user.domain.User;
import org.springframework.lang.Nullable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class QuestionSaveRequestDto {

    @ApiModelProperty(value = "사용자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    @NotNull
    @Email(message = "올바른 형식의 아이디가 아닙니다.")
    private String userEmail;

    @ApiModelProperty(value = "질문 제목", required = true, example = "Spring Security 가르쳐 주실 분")
    @NotNull
    private String queTitle;

    @ApiModelProperty(value = "질문 내용", required = true, example = "Spring Security 개어렵네염")
    @NotNull
    private String queContent;

    @ApiModelProperty(value = "질문 관련 에러메시지", required = false, example = "에러코드는 이래용")
    @Nullable
    private String queError;

    @ApiModelProperty(value = "질문 카테고리", notes = "이거 지금 잘 모르겠음", required = false, example = "Spring Security 개어렵네염")
    @Nullable
    private String queCategory;@Nullable

    @ApiModelProperty(value = "태그 목록", required = false, example = "Java")
    private List<String> tags;

    @Builder
    public QuestionSaveRequestDto(String userEmail, String queTitle, String queContent, String queError, String queCategory) {
        this.userEmail = userEmail;
        this.queTitle = queTitle;
        this.queContent = queContent;
        this.queError = queError;
        this.queCategory = queCategory;
    }

    public Question toEntity(User writer) {
        return Question.builder()
                .writer(writer)
                .queTitle(queTitle)
                .queContents(queContent)
                .queError(queError)
                .queCategory(queCategory)
                .build();
    }
}
