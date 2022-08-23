package org.deco.gachicoding.post.board.application.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import javax.validation.constraints.NotNull;

@Getter
public class BoardListRequestDto {
    @NotNull
    @ApiModelProperty(value = "검색어", required = true, example = "운영")
    private String keyword;

    @NotNull
    private Pageable pageable;

    @Builder
    public BoardListRequestDto(String keyword, Pageable pageable) {
        this.keyword = keyword;
        this.pageable = pageable;
    }
}
