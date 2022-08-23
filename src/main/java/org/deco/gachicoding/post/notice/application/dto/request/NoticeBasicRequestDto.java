package org.deco.gachicoding.post.notice.application.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.post.PostRequestDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Getter
public class NoticeBasicRequestDto implements PostRequestDto {
    @NotNull
    @Email(message = "올바른 형식의 아이디가 아닙니다.")
    @ApiModelProperty(value = "요청자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull
    @ApiModelProperty(value = "공지사항 번호", required = true, example = "1")
    private Long notIdx;

    @Builder
    public NoticeBasicRequestDto(String userEmail, Long notIdx) {
        this.userEmail = userEmail;
        this.notIdx = notIdx;
    }
}
