package org.deco.gachicoding.domain.tag;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "board_tag")
public class BoardTag {
    
    // id 2개 쓰는법 알아보기
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long boardTagIdx;
    private Long boardIdx;
    private Long tagIdx;

    @Builder
    public BoardTag (Long boardIdx, Long tagIdx) {
        this.boardIdx = boardIdx;
        this.tagIdx = tagIdx;
    }
}
