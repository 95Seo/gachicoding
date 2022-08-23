package org.deco.gachicoding.tag.domain.repository;

import org.deco.gachicoding.tag.domain.TagRelation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

// ID 2개 or ID 0개로 해야함
public interface TagRelationRepository extends JpaRepository<TagRelation, Long> {
    List<TagRelation> findAllByArticleIdxAndArticleCategory(Long articleIdx, String type);

    void deleteAllByArticleIdxAndArticleCategory(Long articleIdx, String type);
}
