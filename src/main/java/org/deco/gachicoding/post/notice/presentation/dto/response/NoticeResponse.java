package org.deco.gachicoding.post.notice.presentation.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class NoticeResponse {
    private Long notIdx;

    private String authorEmail;
    private String authorNick;

    private String notTitle;
    private String notContent;
    private Long notViews;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Builder
    public NoticeResponse(Long notIdx, String authorEmail, String authorNick, String notTitle, String notContents, Long notViews, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.notIdx = notIdx;

        this.authorEmail = authorEmail;
        this.authorNick = authorNick;

        this.notTitle = notTitle;
        this.notContent = notContents;
        this.notViews = notViews;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
