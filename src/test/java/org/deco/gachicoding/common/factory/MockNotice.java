package org.deco.gachicoding.common.factory;

import org.deco.gachicoding.user.domain.User;

import java.time.LocalDateTime;

public class MockNotice {
    private MockNotice() {

    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private Long notIdx;
        private User writer;

        private String notTitle;
        private String notContent;
        private Long notViews;
        private Boolean notPin;
        private Boolean notActivated;
        private LocalDateTime notRegdate =
                LocalDateTime.of(2000, 2, 2, 2, 2);

        // notRegdate -> createAt
    }
}
