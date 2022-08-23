package org.deco.gachicoding.comment.dto.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.deco.gachicoding.comment.domain.Comment;
import org.deco.gachicoding.user.domain.User;

import javax.validation.constraints.NotNull;

@Getter
public class CommentSaveRequestDto {

    @NotNull
    @ApiModelProperty(value = "작성자 아이디", required = true, example = "Swagger@swagger.com")
    private String userEmail;

    @NotNull
    @ApiModelProperty(value = "댓글 내용", required = true, example = "이것도 모르시나요~ ㅋㅋ")
    private String commContent;

    @NotNull
    @ApiModelProperty(value = "게시판 카테고리", required = true, example = "QUESTION")
    private String articleCategory;

    @NotNull
    @ApiModelProperty(value = "게시글 번호", required = true, example = "1")
    private Long articleIdx;

    public Comment toEntity(User writer) {
        return Comment.builder()
                .writer(writer)
                .commContents(commContent)
                .articleCategory(articleCategory)
                .articleIdx(articleIdx)
                .build();
    }
}
