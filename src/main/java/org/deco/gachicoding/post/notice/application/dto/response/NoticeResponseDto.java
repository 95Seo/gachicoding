package org.deco.gachicoding.post.notice.application.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NoticeResponseDto {
    private Long notIdx;

    private String authorEmail;
    private String authorNick;

    private String notTitle;
    private String notContents;
    private Long notViews;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

//    private List<TagResponseDto> tags;

    @Builder
    public NoticeResponseDto(Long notIdx, String authorEmail, String authorNick, String notTitle, String notContents, Long notViews, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.notIdx = notIdx;

        this.authorEmail = authorEmail;
        this.authorNick = authorNick;

        this.notTitle = notTitle;
        this.notContents = notContents;
        this.notViews = notViews;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

//    @Override
//    public void setTags(List<TagResponseDto> tags) {
//        this.tags = tags;
//    }
}