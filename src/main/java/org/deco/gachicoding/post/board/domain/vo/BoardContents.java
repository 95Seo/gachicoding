package org.deco.gachicoding.post.board.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BoardContents {

    public static final int MAXIMUM_CONTENT_LENGTH = 10000;

    @Column
    private String boardContents;

    protected BoardContents() {}

    public BoardContents(String boardContents) {
        validateMaximumLength(boardContents);
        this.boardContents = boardContents;
    }

    public String getBoardContents() {
        return boardContents;
    }

    private void validateMaximumLength(String boardContents) {
        // 개발
//        if (boardContent.length() > MAXIMUM_CONTENT_LENGTH)
//            throw new BoardFormatException();
    }
}
