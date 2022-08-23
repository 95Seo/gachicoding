package org.deco.gachicoding.tag.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

// 추후 TagRelation -> 각 도메인별 테그로 변경하자 ex)BoardTag, NoticeTag, ...
// 그렇게 되면 Board나 Notice의 테이블과 각각 관계를 가지는 관계 테이블이 따로 생기겠지?
@Getter
@Entity
@NoArgsConstructor
@Table(name = "tag_rel")
public class TagRelation {

    // 테그 테이터들이 태그 * 글갯수로 쌓인다
    // 너무 많이 쌓이는거 같은데
    // 문자열 처리로 합쳐서 넣어 버릴까?
    @Id @Column(name = "rel_idx")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long relationIdx;
    private String articleCategory;
    private Long articleIdx;

    @ManyToOne
    @JoinColumn(name = "tag_idx")
    private Tag tag;
//    private String tagKeyword;

    @Builder
    public TagRelation(Long articleIdx, Tag tag, String articleCategory, String tagKeyword) {
        this.articleIdx = articleIdx;
        this.tag = tag;
        this.articleCategory = articleCategory;
//        this.tagKeyword = tagKeyword;
    }
}
