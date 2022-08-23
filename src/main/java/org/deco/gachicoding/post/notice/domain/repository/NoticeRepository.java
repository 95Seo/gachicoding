package org.deco.gachicoding.post.notice.domain.repository;

import org.deco.gachicoding.post.notice.domain.Notice;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface NoticeRepository extends JpaRepository<Notice, Long> {

    @Query("SELECT n FROM Notice n LEFT JOIN FETCH n.author WHERE n.notIdx = :notIdx")
    Optional<Notice> findNoticeByIdx(@Param("notIdx") Long notIdx);

    @Query("SELECT n FROM Notice n LEFT JOIN FETCH n.author WHERE n.notLocked = true AND n.notIdx = :notIdx")
    Optional<Notice> findEnableNoticeByIdx(@Param("notIdx") Long notIdx);

    @Query("SELECT DISTINCT n FROM Notice n LEFT JOIN FETCH n.author WHERE n.notLocked = true AND (n.notTitle.notTitle LIKE %:keyword% OR n.notContents.notContents LIKE %:keyword%) ")
    List<Notice> findAllNoticeByKeyword(@Param("keyword") String keyword, Pageable pageable);
}