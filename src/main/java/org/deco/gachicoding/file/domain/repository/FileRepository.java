package org.deco.gachicoding.file.domain.repository;

import org.deco.gachicoding.file.domain.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    List<File> findAllByArticleCategoryAndArticleIdx(String category, Long idx);
}
