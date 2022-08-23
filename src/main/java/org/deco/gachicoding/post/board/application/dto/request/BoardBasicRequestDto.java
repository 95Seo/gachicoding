package org.deco.gachicoding.post.board.application.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class BoardBasicRequestDto {
    @NotNull
    @Email(message = "올바른 형식의 아이디가 아닙니다.")
    @ApiModelProperty(value = "요청자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull
    @ApiModelProperty(value = "게시판 번호", required = true, example = "1")
    private Long boardIdx;

    @Builder
    public BoardBasicRequestDto(String userEmail, Long boardIdx) {
        this.userEmail = userEmail;
        this.boardIdx = boardIdx;
    }
}
