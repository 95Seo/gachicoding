package org.deco.gachicoding.post.board.application.dto.response;

import lombok.Builder;
import lombok.Getter;
import org.deco.gachicoding.post.PostResponseDto;
import org.deco.gachicoding.file.dto.response.FileResponseDto;
import org.deco.gachicoding.tag.dto.response.TagResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class BoardResponseDto implements PostResponseDto {

    private Long boardIdx;
    private String authorEmail;
    private String authorNick;

    private String boardTitle;
    private String boardContents;
    private String boardCategory;
    private Long boardViews;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

//    private List<FileResponseDto> files;
    private List<TagResponseDto> tags;

    @Builder
    public BoardResponseDto(Long boardIdx, String authorEmail, String authorNick, String boardTitle, String boardContents, String boardCategory, Long boardViews, LocalDateTime createAt, LocalDateTime updateAt) {
        this.boardIdx = boardIdx;

        this.authorEmail = authorEmail;
        this.authorNick = authorNick;

        this.boardTitle = boardTitle;
        this.boardContents = boardContents;
        this.boardCategory = boardCategory;
        this.boardViews = boardViews;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    @Override
    public void setFiles(List<FileResponseDto> files) {
//        this.files = files;
    }

    @Override
    public void setTags(List<TagResponseDto> tags) {
        this.tags = tags;
    }
}