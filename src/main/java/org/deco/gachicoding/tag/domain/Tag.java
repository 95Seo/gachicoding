package org.deco.gachicoding.tag.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;

@Getter
@DynamicInsert
@Entity
@NoArgsConstructor
@Table(name = "tag")
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagIdx;
    private String tagKeyword;

    @Builder
    public Tag(String keyword) {
        this.tagKeyword = keyword;
    }
}
