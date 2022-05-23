package org.deco.gachicoding.dto.notice;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.deco.gachicoding.domain.notice.Notice;
import org.deco.gachicoding.domain.user.User;
import org.deco.gachicoding.dto.ResponseDto;
import org.deco.gachicoding.dto.file.FileResponseDto;
import org.deco.gachicoding.dto.tag.TagResponseDto;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class NoticeResponseDto implements ResponseDto {

    private Long notIdx;
    private Long userIdx;
    private String userNick;
    private String userPicture;
    private String notTitle;
    private String notContent;
    private int notViews;
    private Boolean notPin;
    private LocalDateTime notRegdate;

    private List<FileResponseDto> files;

    private List<TagResponseDto> tags;

    @Builder
    public NoticeResponseDto(Notice notice) {
        setWriterInfo(notice);
        this.notIdx = notice.getNotIdx();
        this.notTitle = notice.getNotTitle();
        this.notContent = notice.getNotContent();
        this.notPin = notice.getNotPin();
        this.notRegdate = notice.getNotRegdate();
    }

    private void setWriterInfo(Notice notice) {
        User user = notice.getUser();
        this.userIdx = user.getUserIdx();
        this.userNick = user.getUserNick();
        this.userPicture = user.getUserPicture();
    }

    @Override
    public void setFiles(List<FileResponseDto> files) {
        this.files = files;
    }

    @Override
    public void setTags(List<TagResponseDto> tags) {
        this.tags = tags;
    }
}
