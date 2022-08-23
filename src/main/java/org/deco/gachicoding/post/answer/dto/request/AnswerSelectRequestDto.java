package org.deco.gachicoding.post.answer.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class AnswerSelectRequestDto {

    @NotNull
    @Email(message = "올바른 형식의 아이디가 아닙니다.")
    @ApiModelProperty(value = "사용자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull
    @ApiModelProperty(value = "답변 번호", required = true, example = "1")
    private Long ansIdx;
}
