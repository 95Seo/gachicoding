package org.deco.gachicoding.post.board.application.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
public class BoardSaveRequestDto {
    @NotNull
    @ApiModelProperty(value = "작성자 아이디", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull
    @ApiModelProperty(value = "게시판 제목", required = true, example = "오늘 가입했습니다. 잘 부탁 드립니다.")
    private String boardTitle;

    @NotNull
    @ApiModelProperty(value = "게시판 내용", required = true, example = "안녕하세요 반갑습니다.")
    private String boardContents;

    @NotNull
    @ApiModelProperty(value = "게시판 카테고리", required = false, example = "개발 일상 토론")
    private String boardCategory;

    @Nullable
    @ApiModelProperty(value = "조회수", required = false, example = "0")
    private Long boardViews;

    @Nullable
    @ApiModelProperty(value = "태그 목록", required = false, example = "Java")
    private List<String> tags;

    @Builder
    public BoardSaveRequestDto(String userEmail, String boardTitle, String boardContents, String boardCategory, Long boardViews, List<String> tags) {
        this.userEmail = userEmail;
        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.boardCategory = boardCategory;
        this.boardViews = boardViews;
        this.tags = tags;
    }
}