package org.deco.gachicoding.post.notice.application.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class NoticeDetailRequestDto {

    @NotNull(message = "F0001")
    @ApiModelProperty(value = "공지사항 번호", required = true, example = "1")
    private Long notIdx;

    @Builder
    public NoticeDetailRequestDto(Long notIdx) {
        this.notIdx = notIdx;
    }
}
