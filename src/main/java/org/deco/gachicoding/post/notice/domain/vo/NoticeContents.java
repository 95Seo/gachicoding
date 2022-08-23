package org.deco.gachicoding.post.notice.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class NoticeContents {

    public static final int MAXIMUM_CONTENT_LENGTH = 10000;

    @Column
    private String notContents;

    protected NoticeContents() {}

    public NoticeContents(String notContents) {
        validateMaximumLength(notContents);
        this.notContents = notContents;
    }

    public String getNoticeContents() {
        return notContents;
    }

    private void validateMaximumLength(String notContents) {
        // 개발
//        if (notContent.length() > MAXIMUM_CONTENT_LENGTH)
//            throw new NoticeFormatException();
    }
}
