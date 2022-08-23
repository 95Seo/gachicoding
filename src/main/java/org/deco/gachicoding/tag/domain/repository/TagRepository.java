package org.deco.gachicoding.tag.domain.repository;

import org.deco.gachicoding.tag.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<Tag, Long> {
    Optional<Tag> findByTagKeyword(String keyword);
}
