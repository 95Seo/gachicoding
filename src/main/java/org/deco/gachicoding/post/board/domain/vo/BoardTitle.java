package org.deco.gachicoding.post.board.domain.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class BoardTitle {

    public static final int MAXIMUM_CONTENT_LENGTH = 100;

    @Column
    private String boardTitle;

    protected BoardTitle() {}

    public BoardTitle(String boardTitle) {
        validateMaximumLength(boardTitle);
        this.boardTitle = boardTitle;
    }

    public String getBoardTitle() {
        return boardTitle;
    }

    private void validateMaximumLength(String boardTitle) {
//        if (boardTitle.length() > MAXIMUM_CONTENT_LENGTH)
//            throw new BoardFormatException();
    }
}
