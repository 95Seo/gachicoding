package org.deco.gachicoding.post.board.application.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Getter
public class BoardDetailRequestDto {

    @NotNull(message = "F0001")
    @ApiModelProperty(value = "게시판 번호", required = true, example = "1")
    private Long boardIdx;

    @Builder
    public BoardDetailRequestDto(Long boardIdx) {
        this.boardIdx = boardIdx;
    }
}
