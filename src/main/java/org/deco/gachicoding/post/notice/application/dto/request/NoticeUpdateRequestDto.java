package org.deco.gachicoding.post.notice.application.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.post.PostRequestDto;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
public class NoticeUpdateRequestDto implements PostRequestDto {

    @NotNull(message = "F0001")
    @Email(message = "올바른 형식의 아이디가 아닙니다.")
    @ApiModelProperty(value = "요청자 이메일", notes = "고유한 아이디로 쓰임", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull(message = "F0001")
    @ApiModelProperty(value = "공지사항 번호", required = true, example = "1")
    private Long notIdx;

    @NotNull(message = "F0001")
    @Size(max = 100, message = "F0004")
    @ApiModelProperty(value = "공지사항 제목", required = false, example = "수정된 제목")
    private String notTitle;

    @NotNull(message = "F0001")
    @Size(max = 10000, message = "F0004")
    @ApiModelProperty(value = "공지사항 내용", required = false, example = "수정된 내용")
    private String notContents;

    @Builder
    public NoticeUpdateRequestDto(String userEmail, Long notIdx, String notTitle, String notContents) {
        this.userEmail = userEmail;
        this.notIdx = notIdx;
        this.notTitle = notTitle;
        this.notContents = notContents;
    }
}
