package org.deco.gachicoding.comment.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class CommentUpdateRequestDto {

        @NotNull
        @Email(message = "올바른 형식의 아이디가 아닙니다.")
        @ApiModelProperty(value = "요청자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
        private String userEmail;

        @NotNull
        @ApiModelProperty(value = "댓글 번호", required = true, example = "1")
        private Long commIdx;

        @NotNull
        @ApiModelProperty(value = "댓글 내용", required = true, example = "이것도 모르시나요~ ㅋㅋ")
        private String commContent;
}
