package org.deco.gachicoding.post.notice.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NoticeTitle {

    public static final int MAXIMUM_CONTENT_LENGTH = 100;

    @Column
    private String notTitle;

    protected NoticeTitle() {}

    public NoticeTitle(String notTitle) {
        validateMaximumLength(notTitle);
        this.notTitle = notTitle;
    }

    public String getNoticeTitle() {
        return notTitle;
    }

    private void validateMaximumLength(String notTitle) {
//        if (notTitle.length() > MAXIMUM_CONTENT_LENGTH)
//            throw new NoticeFormatException();
    }
}
