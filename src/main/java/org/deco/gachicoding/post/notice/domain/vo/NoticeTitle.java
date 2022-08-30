package org.deco.gachicoding.post.notice.domain.vo;

import org.deco.gachicoding.exception.ApplicationException;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import static org.deco.gachicoding.exception.StatusEnum.*;

@Embeddable
public class NoticeTitle {

    public static final int MAXIMUM_CONTENT_LENGTH = 100;

    @Column(name = "not_title", columnDefinition = "varchar(255)", nullable = false)
    private String notTitle;

    protected NoticeTitle() {}

    public NoticeTitle(String notTitle) {
        validateNullTitle(notTitle);
        validateEmptyTitle(notTitle);
        validateMaximumLength(notTitle);
        this.notTitle = notTitle;
    }

    public String getNoticeTitle() {
        return notTitle;
    }

    private void validateNullTitle(String notTitle) {
        if (notTitle == null)
            throw new ApplicationException(NULL_TITLE);
    }

    private void validateEmptyTitle(String notTitle) {
        if (notTitle.isEmpty())
            throw new ApplicationException(EMPTY_TITLE);
    }

    private void validateMaximumLength(String notTitle) {
        if (notTitle.length() > MAXIMUM_CONTENT_LENGTH)
            throw new ApplicationException(MAXIMUM_LENGTH_OVER_TITLE);
    }
}
